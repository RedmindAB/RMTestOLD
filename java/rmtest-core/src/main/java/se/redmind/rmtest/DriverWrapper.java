package se.redmind.rmtest;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Assume;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.selenium.grid.DriverConfig;

/**
 * @author Jeremy Comte
 */
public class DriverWrapper<DriverType extends WebDriver> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String description;
    private final Function<DesiredCapabilities, DriverType> function;
    private final DesiredCapabilities capabilities;
    private DriverType driver;

    public DriverWrapper(DesiredCapabilities capabilities, String description, Function<DesiredCapabilities, DriverType> function) {
        this.capabilities = capabilities;
        this.description = description;
        this.function = function;
    }

    public void addDriverConfig(DriverConfig conf) {
        addCapabilities(conf::eval, conf::config);
    }

    public void addCapabilities(BiFunction<DesiredCapabilities, String, Boolean> eval, Consumer<DesiredCapabilities> action) {
        if (eval.apply(capabilities, description)) {
            action.accept(capabilities);
        }
    }

    public String getDescription() {
        return description;
    }

    public boolean isStarted() {
        return driver != null;
    }

    public synchronized DriverType getDriver() {
        if (driver == null) {
            driver = function.apply(capabilities);
            logger.info("Started driver: " + description);
        }
        return driver;
    }

    public void stopDriver() {
        if (isStarted()) {
            driver.quit();
        }
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
            Assume.assumeTrue("This driver doesn't seem to have connectivity to: " + url, false);
        }
    }

    public void driverWaitElementPresent(By pBy, int timeoutInSeconds) {
        new WebDriverWait(getDriver(), timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(pBy));
    }

    @Override
    public String toString() {
        return description;
    }

    public DesiredCapabilities getCapability() {
        return capabilities;
    }

}
