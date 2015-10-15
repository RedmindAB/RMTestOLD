package se.redmind.rmtest.selenium.grid;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.selenium.framework.config.FrameworkConfig;

public class Parallelized extends Parameterized {

    int noThreads = getChildren().size();

    private static class ThreadPoolScheduler implements RunnerScheduler {

        private final static Logger LOGGER = LoggerFactory.getLogger(ThreadPoolScheduler.class);

        private final ExecutorService executor;

        public ThreadPoolScheduler(int numChildren) {
            String threads = System.getProperty("junit.parallel.threads", String.valueOf(numChildren));
            LOGGER.info("Number of treads executing in parrallel: " + numChildren);
            int numThreads = Integer.parseInt(threads);
            executor = Executors.newFixedThreadPool(numThreads);
        }

        @Override
        public void finished() {
            executor.shutdown();
            try {
                executor.awaitTermination(20, TimeUnit.MINUTES);
            } catch (InterruptedException exc) {
                throw new RuntimeException(exc);
            }
        }

        @Override
        public void schedule(Runnable childStatement) {
            executor.submit(childStatement);
        }
    }

    public Parallelized(Class<?> klass) throws Throwable {
        super(klass);
        try {
            setScheduler(new ThreadPoolScheduler(noThreads));
        } catch (IllegalArgumentException e) {
            LoggerFactory.getLogger(this.getClass()).error("No drivers found with current filter");
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        if (FrameworkConfig.getConfig().autoCloseDrivers()) {
            notifier.addListener(new AutoCloseListener());
        }
        super.run(notifier);
    }
}
