package se.redmind.rmtest.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Assume;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.Lists;
import se.redmind.rmtest.DriverWrapper;
import se.redmind.utils.Try;

/**
 * @author Jeremy Comte
 */
public abstract class LocalConfiguration<DriverType extends WebDriver> extends DriverConfiguration<DriverType> {

    private boolean imAFailure;
    private final Function<DesiredCapabilities, DriverType> function;

    public LocalConfiguration(DesiredCapabilities baseCapabilities, Function<DesiredCapabilities, DriverType> function) {
        super(baseCapabilities);
        this.function = function;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<DriverWrapper<DriverType>> createDrivers() {
        int maxRetryAttempts = 5;
        if (imAFailure) {
            Assume.assumeTrue("Since driver didn't start after  " + maxRetryAttempts + " attempts, it probably won't start now ", false);
        } else {
            DriverWrapper<DriverType> driver = Try
                .toGet(() -> new DriverWrapper<>(generateCapabilities(), generateDescription(), function))
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

}
