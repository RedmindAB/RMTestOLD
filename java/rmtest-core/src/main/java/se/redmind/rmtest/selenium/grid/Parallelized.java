package se.redmind.rmtest.selenium.grid;

import se.redmind.rmtest.selenium.framework.config.FrameworkConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;

public class Parallelized extends Parameterized {
	int noThreads = getChildren().size();

	private static class ThreadPoolScheduler implements RunnerScheduler {

		private ExecutorService executor;

		public ThreadPoolScheduler(int numChildren) {
			String threads = System.getProperty("junit.parallel.threads", String.valueOf(numChildren));
			System.out.println("Number of treads executing in parrallel: " + numChildren);
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

	public Parallelized(Class klass) throws Throwable {
		super(klass);

		try {
			setScheduler(new ThreadPoolScheduler(noThreads));
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR: No drivers found with current filter");
		}

	}

	@Override
	public void run(RunNotifier notifier) {
		if (FrameworkConfig.getConfig().autoCloseDrivers())
			notifier.addListener(new AutoCloseListener());
		super.run(notifier);
	}
}