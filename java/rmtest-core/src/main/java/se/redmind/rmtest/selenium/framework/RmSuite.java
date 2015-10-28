package se.redmind.rmtest.selenium.framework;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import se.redmind.rmtest.config.Configuration;
import se.redmind.rmtest.config.GridConfiguration;
import se.redmind.rmtest.selenium.grid.AutoCloseListener;
import se.redmind.rmtest.selenium.livestream.LiveStreamListener;

public class RmSuite extends Suite {

    private LiveStreamListener liveStreamListener;

    public RmSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }

    @Override
    public void run(RunNotifier notifier) {
        Configuration config = Configuration.current();
        if (config.runner instanceof GridConfiguration && config.runner.as(GridConfiguration.class).enableLiveStream) {
            liveStreamListener = new LiveStreamListener();
            notifier.addListener(liveStreamListener);
        }
        if (config.autoCloseDrivers) {
            notifier.addListener(new AutoCloseListener());
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
}
