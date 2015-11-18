package se.redmind.rmtest.config;

import se.redmind.rmtest.DriverWrapper;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import se.redmind.rmtest.selenium.framework.Browser;

/**
 * @author Jeremy Comte
 */
@JsonTypeName("firefox")
public class FirefoxConfiguration extends LocalConfiguration<FirefoxDriver> {

    public FirefoxConfiguration() {
        super(DesiredCapabilities.firefox());
    }

    @Override
    protected DriverWrapper<FirefoxDriver> createDriver() {
        return new DriverWrapper<>(baseCapabilities, Browser.Firefox.toString(), (capabilities) -> new FirefoxDriver(capabilities));
    }

}
