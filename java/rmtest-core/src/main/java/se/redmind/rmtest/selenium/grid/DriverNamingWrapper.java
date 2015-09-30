package se.redmind.rmtest.selenium.grid;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assume;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.redmind.rmtest.selenium.framework.Browser;

public class DriverNamingWrapper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<DriverConfig> driverConfigs = new ArrayList<>();
    private final DesiredCapabilities capability;
    private final String description;
    private URL url;
    private WebDriver driver;
    boolean imAFailure;
    private Browser browser;

    public DriverNamingWrapper(URL url, DesiredCapabilities capability, String description) {
        this.url = url;
        this.capability = capability;
        this.description = description;
        this.imAFailure = false;
    }

    public DriverNamingWrapper(Browser browser, String description) {
        this.browser = browser;
        this.description = description;
        this.capability = getLocalCapability();
    }

    private DesiredCapabilities getLocalCapability() {
        switch (browser) {
            case Chrome:
                return DesiredCapabilities.chrome();
            case Firefox:
                return DesiredCapabilities.firefox();
            case PhantomJS:
                return DesiredCapabilities.phantomjs();
            default:
                return null;
        }
    }

    public String getDescription() {
        return description;
    }

    public URL getUrl() {
        return url;
    }

    public DesiredCapabilities getCapability() {
        return capability;
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public void ignoreAtNoConnectivityById(String url, String id) {
        ignoreAtNoConnectivityTo(url, By.id(id));
    }

    public void ignoreAtNoConnectivityByClass(String url, String className) {
        ignoreAtNoConnectivityTo(url, By.className(className));
    }

    public void ignoreAtNoConnectivityByXpath(String url, String xpath) {
        ignoreAtNoConnectivityTo(url, By.xpath(xpath));
    }

    public void ignoreAtNoConnectivityTo(String url, By by) {
        try {
            getDriver().get(url);
            driverWaitElementPresent(by, 10);
        } catch (NoSuchElementException | TimeoutException e) {
            this.imAFailure = true;
            Assume.assumeTrue("This driver doesn't seem to have connectivity to: " + url, false);

        }
    }

    public void addDriverConfig(DriverConfig conf) {
        driverConfigs.add(conf);
    }

    /**
     * @param pBy
     * @param timeoutInSeconds
     */
    public void driverWaitElementPresent(By pBy, int timeoutInSeconds) {
        new WebDriverWait(getDriver(), timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(pBy));
    }

    /**
     * @param filteredUrlCapList
     */
    public WebDriver startDriver() {
        setupCapabilities();
        if (this.driver == null) {
            if (browser != null) {
                this.driver = startLocalDriver(this.browser);
            } else {
                int maxRetryAttempts = 5;
                if (this.imAFailure) {
                    Assume.assumeTrue("Since driver didn't start after  " + maxRetryAttempts + " attempts, it probably wont start now ", false);
                    return this.driver;
                } else {
                    int retryAttempts = 1;

                    startDriver:
                    {
                        while (retryAttempts <= maxRetryAttempts) {
                            try {
                                if (this.driver != null) {
                                    this.driver.close();
                                }
                                if (capability.getCapability("rmDeviceType") == null) {
                                    this.driver = new RemoteWebDriver(url, capability);
                                    logger.info("This is a RemoteWebDriver");
                                } else {

                                    if ("Android".equalsIgnoreCase((String) capability.getCapability("platformName"))) {
                                        this.driver = new AndroidDriver(url, capability);
                                    } else {
                                        this.driver = new IOSDriver(url, capability);
                                    }
                                    logger.info("This is a AppiumDriver");
                                }
                                logger.info("Started driver: " + description);
                            } catch (Exception e) {
                                logger.warn("Having trouble starting webdriver for device: " + this.description, e);
                                logger.warn("Attempt " + retryAttempts + " of " + maxRetryAttempts);
                                retryAttempts++;
                                continue;
                            }
                            break startDriver;
                        }
                        this.imAFailure = true;
                        Assume.assumeTrue("Driver failed to start properly after " + (retryAttempts - 1) + " attempts", false);
                    }
                }
            }
        }
        return this.driver;
    }

    private void setupCapabilities() {
        driverConfigs.stream()
            .filter(driverConfig -> driverConfig.eval(capability, description))
            .forEach(driverConfig -> driverConfig.config(capability));
    }

    private WebDriver startLocalDriver(Browser browser) {
        WebDriver driver = null;
        switch (browser) {
            case Chrome:
                System.setProperty("webdriver.chrome.driver", getChromePath());
                driver = new ChromeDriver(capability);
                break;
            case Firefox:
                driver = new FirefoxDriver(capability);
                break;
            case PhantomJS:
                final PhantomJSDesiredCapabalities phantomJS = new PhantomJSDesiredCapabalities();
                driver = new PhantomJSDriver(phantomJS.createPhantomJSCapabilities());
                break;
            default:
                break;
        }
        return driver;
    }

    private String getChromePath() {
        String osName = System.getProperty("os.name");
        String _default = TestHome.main() + "/lib/chromedriver";
        if (osName.startsWith("Mac")) {
            System.out.println("Setting default chromedriver");
            return _default;
        } else if (osName.startsWith("Linux")) {
            System.out.println("Setting linux chromedriver");
            return TestHome.main() + "/lib/linux/chromedriver";
        }
        return _default;
    }

    @Override
    public String toString() {
        return description;
    }
}
