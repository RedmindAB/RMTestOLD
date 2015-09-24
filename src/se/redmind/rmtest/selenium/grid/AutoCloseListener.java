package se.redmind.rmtest.selenium.grid;

import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class AutoCloseListener extends RunListener {

    @Override
    public void testRunFinished(Result result) throws Exception {
        DriverProvider.stopDrivers();
    }
}
