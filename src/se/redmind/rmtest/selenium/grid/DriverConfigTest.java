package se.redmind.rmtest.selenium.grid;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverConfigTest implements DriverConfig {

	@Override
	public boolean eval(DesiredCapabilities capabilities, String description) {
		return capabilities.getBrowserName().equals("firefox");
	}

	@Override
	public void config(DesiredCapabilities capabilities) {
		capabilities.setCapability(FirefoxDriver.PROFILE, new FirefoxProfile());
	}

}
