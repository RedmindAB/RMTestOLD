package se.redmind.rmtest.selenium.framework;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import se.redmind.rmtest.selenium.framework.config.FrameworkConfig;
import se.redmind.rmtest.selenium.grid.AutoCloseListener;
import se.redmind.rmtest.selenium.livestream.LiveStreamListener;

public class RmSuite extends Suite {

    private LiveStreamListener liveStreamListener;
    private final FrameworkConfig config = FrameworkConfig.getConfig();

    public RmSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }

    @Override
    public void run(RunNotifier notifier) {
        if (config.enableLiveStream() && config.runOnGrid()) {
            liveStreamListener = new LiveStreamListener();
            notifier.addListener(liveStreamListener);
        }
        if (config.autoCloseDrivers()) {
            notifier.addListener(new AutoCloseListener());
        }
        notifier.fireTestRunStarted(getDescription());
        super.run(notifier);
    }

    @Override
    protected void runChild(Runner runner, RunNotifier notifier) {
        if (config.enableLiveStream() && liveStreamListener != null) {
            notifier.addListener(liveStreamListener.getSubListener());
        }
        super.runChild(runner, notifier);
    }
}
