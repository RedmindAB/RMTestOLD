package se.redmind.rmtest.selenium.framework;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import se.redmind.rmtest.selenium.grid.AutoCloseListener;
import se.redmind.rmtest.selenium.grid.RmConfig;
import se.redmind.rmtest.selenium.livestream.LiveStreamListener;
import se.redmind.rmtest.selenium.livestream.RmTestResultBuilder;

public class RmSuite extends Suite {

	private LiveStreamListener liveStreamListener;
	private volatile RmTestResultBuilder resBuilder;

	public RmSuite(Class<?> klass, RunnerBuilder builder)
			throws InitializationError {
		super(klass, builder);
	}
	
	@Override
	public void run(RunNotifier notifier) {
		resBuilder = new RmTestResultBuilder();
		if (RmConfig.enableLiveStream()){
			liveStreamListener = new LiveStreamListener();
			notifier.addListener(liveStreamListener);
		}
		if (RmConfig.autoCloseDrivers()) notifier.addListener(new AutoCloseListener());
		notifier.fireTestRunStarted(getDescription());
		super.run(notifier);
	}
	
	@Override
	protected void runChild(Runner runner, RunNotifier notifier) {
		if (RmConfig.enableLiveStream()) notifier.addListener(liveStreamListener.getSubListener());
		super.runChild(runner, notifier);
	}
	
	

}
