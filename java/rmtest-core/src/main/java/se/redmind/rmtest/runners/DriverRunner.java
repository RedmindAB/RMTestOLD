package se.redmind.rmtest.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.config.Configuration;
import se.redmind.rmtest.config.GridConfiguration;
import se.redmind.rmtest.selenium.livestream.LiveStreamListener;
import se.redmind.utils.Fields;

/**
 * Parameterized runner that will pick up all the drivers configuration and inject our test classes automatically
 *
 * @author Jeremy Comte
 */
public class DriverRunner extends Parameterized {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DriverRunner.class);
    private LiveStreamListener liveStreamListener;

    public DriverRunner(Class<?> klass) throws Throwable {
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
            LOGGER.info("will run " + nThreads + " test" + (nThreads > 1 ? "s" : "") + " in parallel");
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
        Optional<DriverWrapper<?>> driverWrapper = getCurrentDriverWrapper(runner);

        if (driverWrapper.isPresent()) {
            try {
                ((BlockJUnit4ClassRunnerWithParameters) runner).filter(new Filter() {
                    @Override
                    public boolean shouldRun(Description description) {
                        FilterDrivers filterDrivers = description.getAnnotation(FilterDrivers.class);
                        if (filterDrivers != null) {
                            if (!DriverWrapper.filter(filterDrivers).test(driverWrapper.get())) {
                                notifier.fireTestIgnored(description);
                                return false;
                            }
                        }
                        return true;
                    }

                    @Override
                    public String describe() {
                        return "FilterDrivers";
                    }
                });
            } catch (NoTestsRemainException ex) {
                return;
            }

            if (!getTestClass().getJavaClass().isAnnotationPresent(ReuseDriverBetweenTests.class)) {
                notifier.addListener(new RunListener() {

                    @Override
                    public void testFinished(Description description) throws Exception {
                        driverWrapper.get().stopDriver();
                    }

                });
            }
        }

        if (liveStreamListener != null) {
            notifier.addListener(liveStreamListener.getSubListener());
        }

        super.runChild(runner, notifier);
        if (driverWrapper.isPresent() && Configuration.current().autoCloseDrivers) {
            driverWrapper.get().stopDriver();
        }
    }

    protected Optional<DriverWrapper<?>> getCurrentDriverWrapper(Runner runner) {
        if (runner instanceof BlockJUnit4ClassRunnerWithParameters) {
            try {
                Object[] parameters = Fields.getValue(runner, "parameters");
                for (Object parameter : parameters) {
                    if (parameter instanceof DriverWrapper) {
                        return Optional.of((DriverWrapper<?>) parameter);
                    }
                }
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return Optional.empty();
    }

    @Override
    protected TestClass createTestClass(Class<?> testClass) {
        return new TestClass(testClass) {

            private final TestClass runnerAsTestClass = new TestClass(DriverRunner.this.getClass()) {
                @Override
                protected void scanAnnotatedMembers(Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations,
                                                    Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations) {
                    try {
                        addToAnnotationLists(new FrameworkMethod(DriverRunner.this.getClass().getMethod("getDriversAsParameters")) {

                            @Override
                            protected int getModifiers() {
                                return super.getModifiers() | Modifier.STATIC;
                            }

                            @Override
                            public Object invokeExplosively(Object target, Object... params) throws Throwable {
                                return super.invokeExplosively(DriverRunner.this, params);
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
            drivers = Configuration.current().createWrappersParameters(filterDrivers);
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
