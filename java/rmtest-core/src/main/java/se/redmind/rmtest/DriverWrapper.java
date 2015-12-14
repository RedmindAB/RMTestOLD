package se.redmind.rmtest;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Assume;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import se.redmind.rmtest.runners.Capability;
import se.redmind.rmtest.runners.FilterDrivers;
import se.redmind.rmtest.selenium.framework.Browser;
import se.redmind.rmtest.selenium.grid.DriverConfig;
import se.redmind.utils.ThrowingRunnable;

/**
 * @author Jeremy Comte
 */
public class DriverWrapper<DriverType extends WebDriver> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Function<DesiredCapabilities, DriverType> function;
    private final DesiredCapabilities capabilities;
    private final String description;
    private DriverType driver;
    private final Set<ThrowingRunnable> preConfigurations = new LinkedHashSet<>();
    private final Set<Consumer<DriverType>> postConfigurations = new LinkedHashSet<>();

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

    public void addPreConfiguration(ThrowingRunnable preConfiguration) {
        preConfigurations.add(preConfiguration);
    }

    public void addPostConfiguration(Consumer<DriverType> postConfiguration) {
        postConfigurations.add(postConfiguration);
    }

    public String getDescription() {
        return description;
    }

    public boolean isStarted() {
        return driver != null;
    }

    public synchronized DriverType getDriver() {
        if (driver == null) {
            preConfigurations.forEach(preConfiguration -> {
                try {
                    preConfiguration.run();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            driver = function.apply(capabilities);
            postConfigurations.forEach(postConfiguration -> postConfiguration.accept(driver));
            logger.info("Started driver [" + description + "]");
        }
        return driver;
    }

    public void stopDriver() {
        if (isStarted()) {
            logger.info("Closing driver [" + description + "]");
            try {
                driver.quit();
            } catch (UnreachableBrowserException e) {
                logger.error(e.getMessage());
            }
            driver = null;
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

    public FluentWait<DriverType> driverFluentWait(int timeoutInSeconds) {
        FluentWait<DriverType> fw = null;
        for (int i = 0; i < 10; i++) {
            try {
                fw = new FluentWait<>(getDriver()).withTimeout(timeoutInSeconds, TimeUnit.SECONDS);
                fw.ignoring(WebDriverException.class, ClassCastException.class);
                fw.ignoring(NoSuchElementException.class);
                return fw;
            } catch (Exception e) {
                if (i >= 9) {
                    logger.warn("driverFluentWait Failed attempt : " + i + "/n" + e);
                }
            }
        }
        if (fw == null) {
            throw new WebDriverException("driverFluentWait failed after ten attempts");
        } else {
            return fw;
        }
    }

    @Override
    public String toString() {
        return description;
    }

    public DesiredCapabilities getCapability() {
        return capabilities;
    }

    public static Predicate<DriverWrapper<?>> filter(FilterDrivers filterDrivers) {
        return filter(filterDrivers.platforms())
            .and(filter(filterDrivers.types()))
            .and(filter(filterDrivers.browsers()))
            .and(filter(filterDrivers.capabilities()));
    }

    public static Predicate<DriverWrapper<?>> filter(Platform... values) {
        return driver -> {
            Set<Platform> platforms = Sets.newHashSet(values);
            return platforms.isEmpty() || platforms.contains(driver.getCapability().getPlatform());
        };
    }

    public static Predicate<DriverWrapper<?>> filter(Class<? extends DriverWrapper<?>>... values) {
        return driver -> {
            Set<Class<? extends DriverWrapper<?>>> types = Sets.newHashSet(values);
            return types.isEmpty() || types.contains((Class<? extends DriverWrapper<?>>) driver.getClass());
        };
    }

    public static Predicate<DriverWrapper<?>> filter(Browser... values) {
        return driver -> {
            Set<String> browsers = Sets.newHashSet(values).stream().map(value -> value.toString().toLowerCase()).collect(Collectors.toSet());
            return browsers.isEmpty() || browsers.contains(driver.getCapability().getBrowserName());
        };
    }

    public static Predicate<DriverWrapper<?>> filter(Capability... values) {
        return driver -> {
            Set<Capability> capabilities = Sets.newHashSet(values);
            return capabilities.isEmpty() || capabilities.stream().allMatch(capability -> {
                String currCap = (String) driver.getCapability().getCapability(capability.name());
                if (currCap == null) {
                    currCap = "";
                }
                return currCap.equalsIgnoreCase(capability.value());
            });
        };
    }

}
