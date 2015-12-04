package se.redmind.rmtest.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.TestClass;
import org.openqa.selenium.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.config.Configuration;
import se.redmind.rmtest.config.GridConfiguration;
import se.redmind.rmtest.selenium.framework.Browser;
import se.redmind.rmtest.selenium.livestream.LiveStreamListener;

public class RmTestRunner extends Parameterized {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RmTestRunner.class);
    private LiveStreamListener liveStreamListener;

    public RmTestRunner(Class<?> klass) throws Throwable {
        super(klass);
        if (klass.isAnnotationPresent(Parallelize.class)) {
            String threads = System.getProperty("junit.parallel.threads");
            int nThreads;
            if (threads != null && threads.matches("[0-9]+")) {
                nThreads = Integer.parseInt(threads);
            } else {
                Parallelize parallelize = klass.getAnnotation(Parallelize.class);
                nThreads = parallelize.threads() > -1 ? parallelize.threads() : (Runtime.getRuntime().availableProcessors() / 2) + 1;
            }
            LOGGER.debug("will run " + nThreads + " test" + (nThreads > 1 ? "s" : "") + " in parallel");
            setScheduler(new ThreadPoolScheduler(nThreads));
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        if (Configuration.current().drivers.stream().anyMatch(driver -> driver instanceof GridConfiguration && driver.as(GridConfiguration.class).enableLiveStream)) {
            liveStreamListener = new LiveStreamListener();
            notifier.addListener(liveStreamListener);
        }
        notifier.fireTestRunStarted(getDescription());
        super.run(notifier);
    }

    @Override
    protected void runChild(Runner runner, RunNotifier notifier) {
        if (liveStreamListener != null) {
            notifier.addListener(liveStreamListener.getSubListener());
        }
        super.runChild(runner, notifier);
    }

    @Override
    protected TestClass createTestClass(Class<?> testClass) {
        return new TestClass(testClass) {

            private final TestClass runnerAsTestClass = new TestClass(RmTestRunner.this.getClass()) {
                @Override
                protected void scanAnnotatedMembers(Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations,
                                                    Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations) {
                    try {
                        addToAnnotationLists(new FrameworkMethod(RmTestRunner.this.getClass().getMethod("getDriversAsParameters")) {

                            @Override
                            protected int getModifiers() {
                                return super.getModifiers() | Modifier.STATIC;
                            }

                            @Override
                            public Object invokeExplosively(Object target, Object... params) throws Throwable {
                                return super.invokeExplosively(RmTestRunner.this, params);
                            }

                        }, methodsForAnnotations);
                    } catch (NoSuchMethodException | SecurityException ex) {
                        throw new RuntimeException(ex);
                    }
                    super.scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);
                }
            };

            @Override
            public List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> annotationClass) {
                List<FrameworkMethod> methods = super.getAnnotatedMethods(annotationClass);
                if (methods.isEmpty()) {
                    return runnerAsTestClass.getAnnotatedMethods(annotationClass);
                }
                return methods;
            }
        };
    }

    @Parameterized.Parameters
    public Collection<Object[]> getDriversAsParameters() {
        Collection<Object[]> drivers;
        if (getTestClass().getJavaClass().isAnnotationPresent(FilterDrivers.class)) {
            FilterDrivers filterDrivers = getTestClass().getJavaClass().getAnnotation(FilterDrivers.class);
            Set<Platform> platforms = Sets.newHashSet(filterDrivers.platforms());
            Set<Capability> capabilities = Sets.newHashSet(filterDrivers.capabilities());
            Set<Class<? extends DriverWrapper<?>>> types = Sets.newHashSet(filterDrivers.types());
            Set<Browser> browsers = Sets.newHashSet(filterDrivers.browsers());

            drivers = Configuration.current().createWrappers().stream()
                .filter(driver -> platforms.isEmpty() || platforms.contains(driver.getCapability().getPlatform()))
                .filter(driver -> types.isEmpty() || types.contains((Class<? extends DriverWrapper<?>>) driver.getClass()))
                .filter(driver -> browsers.isEmpty() || browsers.contains(Browser.valueOf(driver.getCapability().getBrowserName())))
                .filter(driver -> capabilities.isEmpty() || capabilities.stream().allMatch(capability -> {
                    String currCap = (String) driver.getCapability().getCapability(capability.name());
                    if (currCap == null) {
                        currCap = "";
                    }
                    return currCap.equalsIgnoreCase(capability.value());
                }))
                .map(obj -> new Object[]{obj}).collect(Collectors.toList());
            if (drivers.isEmpty()) {
                LOGGER.warn("we didn't find any driver matching our filter " + filterDrivers);
            }
        } else {
            drivers = Configuration.current().createWrappersParameters();
            if (drivers.isEmpty()) {
                LOGGER.warn("we didn't find any driver");
            }
        }

        return drivers;
    }

    private static class ThreadPoolScheduler implements RunnerScheduler {

        private final ExecutorService executor;

        public ThreadPoolScheduler(int nThreads) {
            executor = Executors.newFixedThreadPool(nThreads);
        }

        @Override
        public void finished() {
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException exc) {
                throw new RuntimeException(exc);
            }
        }

        @Override
        public void schedule(Runnable runnable) {
            executor.submit(runnable);
        }
    }
}
