package se.redmind.rmtest.config;

import se.redmind.rmtest.DriverWrapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assume;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.Lists;
import se.redmind.utils.Try;

/**
 * @author Jeremy Comte
 */
public abstract class LocalConfiguration<DriverType extends WebDriver> extends DriverConfiguration<DriverType> {

    private boolean imAFailure;

    public LocalConfiguration(DesiredCapabilities baseCapabilities) {
        super(baseCapabilities);
    }
 
    @Override
    protected List<DriverWrapper<DriverType>> createDrivers() {
        int maxRetryAttempts = 5;
        if (imAFailure) {
            Assume.assumeTrue("Since driver didn't start after  " + maxRetryAttempts + " attempts, it probably won't start now ", false);
        } else {
            DriverWrapper<DriverType> driver = Try
                .toGet(() -> createDriver())
                .onError((t, e) -> {
                    logger.warn("Having trouble starting webdriver for device: ", e);
                    logger.warn("Attempt " + t.currentAttempt() + " of " + t.maxAttempts());
                })
                .onLastError((t, e) -> {
                    imAFailure = true;
                    Assume.assumeTrue("Driver failed to start properly after " + maxRetryAttempts + " attempts", false);
                })
                .nTimes(maxRetryAttempts);
            if (driver != null) {
                return Lists.newArrayList(driver);
            } else {
                Assume.assumeTrue("Driver was null", false);
            }
        }
        return new ArrayList<>();
    }

    protected abstract DriverWrapper<DriverType> createDriver();

}
