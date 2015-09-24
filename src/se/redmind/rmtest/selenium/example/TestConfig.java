package se.redmind.rmtest.selenium.example;

import se.redmind.rmtest.selenium.grid.DriverConfig;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestConfig implements DriverConfig {

    @Override
    public boolean eval(DesiredCapabilities capabilities, String description) {
        return capabilities.getBrowserName().equals("firefox");
    }

    @Override
    public void config(DesiredCapabilities capabilities) {
        FirefoxProfile ffp = new FirefoxProfile();
        ffp.setPreference("webdriver.load.strategy", "unstable");
        capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");
        capabilities.setCapability(FirefoxDriver.PROFILE, ffp);
    }
}
