package se.redmind.rmtest.selenium.grid;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.config.Configuration;

public class Parallelized extends Parameterized {

    public Parallelized(Class<?> klass) throws Throwable {
        super(klass);
        try {
            setScheduler(new ThreadPoolScheduler());
        } catch (IllegalArgumentException e) {
            LoggerFactory.getLogger(this.getClass()).error("No drivers found with current filter");
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        if (Configuration.current().autoCloseDrivers) {
            notifier.addListener(new AutoCloseListener());
        }
        super.run(notifier);
    }

    private static class ThreadPoolScheduler implements RunnerScheduler {

        private final static Logger LOGGER = LoggerFactory.getLogger(ThreadPoolScheduler.class);

        private final ExecutorService executor;

        public ThreadPoolScheduler() {
            String threads = System.getProperty("junit.parallel.threads");
            int nThreads;
            if (threads != null && threads.matches("[0-9]+")) {
                nThreads = Integer.parseInt(threads);
            } else {
                nThreads = (Runtime.getRuntime().availableProcessors() / 2) + 1;
            }
            LOGGER.info("will run " + nThreads + " test" + (nThreads > 1 ? "s" : "") + " in parrallel:");
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
            DriverProvider.stopDrivers();
        }

        @Override
        public void schedule(Runnable runnable) {
            executor.submit(runnable);
        }
    }
}
