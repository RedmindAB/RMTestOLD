package se.remind.rmtest.selenium.grid;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author petost
 * The only reason this class exists is to fool Junitparams tests to name the tests according to the constructor in this class.
 * 
 */
public class DriverNamingWrapper {
    private String driverDescription = "";
    private WebDriver driver;
    private DesiredCapabilities capabilities;
    private URL driverUrl;
    
    public DriverNamingWrapper(String pDriverDesciption, WebDriver pDriver, DesiredCapabilities pCapabilities, URL pUrl) {
        driverDescription = pDriverDesciption;
        driver = pDriver;
        capabilities = pCapabilities;
        driverUrl = pUrl;
    }
    
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }
    
    public String getDriverDescription() {
        return driverDescription;
    }
    
    public WebDriver getDriver() {
        return driver;
    }
    
    public URL getUrl() {
        return driverUrl;
    }
    
    @Override
    public String toString() {
        return driverDescription;
    }
}
