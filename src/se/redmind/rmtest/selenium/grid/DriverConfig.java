package se.redmind.rmtest.selenium.grid;

import org.openqa.selenium.remote.DesiredCapabilities;

public interface DriverConfig {

    public boolean eval(DesiredCapabilities capabilities, String description);

    public void config(DesiredCapabilities capabilities);
}
