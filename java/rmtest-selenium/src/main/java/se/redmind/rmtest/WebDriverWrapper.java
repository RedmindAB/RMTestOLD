package se.redmind.rmtest;

import java.util.HashSet;
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

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import se.redmind.rmtest.runners.Capability;
import se.redmind.rmtest.runners.FilterDrivers;
import se.redmind.rmtest.selenium.framework.Browser;
import se.redmind.rmtest.selenium.grid.DriverConfig;
import se.redmind.utils.ThrowingRunnable;
import se.redmind.utils.Try;

/**
 * This is a wrapper around a ThreadLocal WebDriver instance.
 *
 * This allow us to parallelize a test on multiple webdrivers as well as using the same webdriver configuration for multiple threads at the same time.
 *
 * @author Jeremy Comte
 */
public class WebDriverWrapper<WebDriverType extends WebDriver> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Function<DesiredCapabilities, WebDriverType> function;
    private final DesiredCapabilities capabilities;
    private final String description;

    private final Set<WebDriverType> openDrivers = new LinkedHashSet<>();
    private final ThreadLocal<Boolean> isStarted = ThreadLocal.withInitial(() -> false);
    private final ThreadLocal<WebDriverType> driverInstance = new ThreadLocal<WebDriverType>() {

        @Override
        protected WebDriverType initialValue() {
            preConfigurations.forEach(preConfiguration -> {
                try {
                    preConfiguration.run();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            WebDriverType driver = function.apply(capabilities);
            postConfigurations.forEach(postConfiguration -> postConfiguration.accept(driver));
            isStarted.set(true);
            logger.info("Started driver [" + description + "]");
            openDrivers.add(driver);
            return driver;
        }

    };
    private final Set<ThrowingRunnable> preConfigurations = new LinkedHashSet<>();
    private final Set<Consumer<WebDriverType>> postConfigurations = new LinkedHashSet<>();

    private boolean reuseDriverBetweenTests;

    public WebDriverWrapper(DesiredCapabilities capabilities, String description, Function<DesiredCapabilities, WebDriverType> function) {
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

    public void addPostConfiguration(Consumer<WebDriverType> postConfiguration) {
        postConfigurations.add(postConfiguration);
    }

    public String getDescription() {
        return description;
    }

    public boolean isStarted() {
        synchronized (driverInstance) {
            return isStarted.get();
        }
    }

    public WebDriverType getDriver() {
        synchronized (driverInstance) {
            return driverInstance.get();
        }
    }

    public boolean reuseDriverBetweenTests() {
        return reuseDriverBetweenTests;
    }

    public void setReuseDriverBetweenTests(boolean reuseDriverBetweenTests) {
        this.reuseDriverBetweenTests = reuseDriverBetweenTests;
    }

    public void stopDriver() {
        synchronized (driverInstance) {
            if (isStarted.get()) {
                openDrivers.remove(driverInstance.get());
                logger.info("Closing driver [" + description + "]");
                try {
                    driverInstance.get().quit();
                } catch (UnreachableBrowserException e) {
                    logger.error(e.getMessage());
                }
                driverInstance.remove();
                isStarted.remove();
            }
        }
    }

    public void stopAllDrivers() {
        openDrivers.forEach(driver -> driver.quit());
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

    public FluentWait<WebDriverType> driverFluentWait(int timeoutInSeconds) {
        return Try.toGet(() -> {
            FluentWait<WebDriverType> fluentWait = new FluentWait<>(getDriver()).withTimeout(timeoutInSeconds, TimeUnit.SECONDS);
            fluentWait.ignoring(WebDriverException.class, ClassCastException.class);
            fluentWait.ignoring(NoSuchElementException.class);
            return fluentWait;
        })
            .onError((t, e) -> logger.warn("driverFluentWait Failed attempt : " + t.currentAttempt() + "/n" + e))
            .onLastError((t, e) -> {
                throw new WebDriverException("driverFluentWait failed after ten attempts");
            })
            .nTimes(10);
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public DesiredCapabilities getCapability() {
        return capabilities;
    }

    public static Predicate<WebDriverWrapper<?>> filter(FilterDrivers filterDrivers) {
        return filter(filterDrivers.platforms())
            .and(filter(filterDrivers.types()))
            .and(filter(filterDrivers.browsers()))
            .and(filter(filterDrivers.capabilities()));
    }

    public static Predicate<WebDriverWrapper<?>> filter(Platform... values) {
        Set<Platform> platforms = Sets.newHashSet(values);
        return driverWrapper -> {
            return platforms.isEmpty() || platforms.contains(driverWrapper.getCapability().getPlatform());
        };
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static Predicate<WebDriverWrapper<?>> filter(Class<? extends WebDriverWrapper<?>>... values) {
        Set<Class<? extends WebDriverWrapper<?>>> types = Sets.newHashSet(values);
        return driverWrapper -> types.isEmpty() || types.contains((Class<? extends WebDriverWrapper<?>>) driverWrapper.getClass());
    }

    public static Predicate<WebDriverWrapper<?>> filter(Browser... values) {
        Set<String> browsers = Sets.newHashSet(values).stream().map(value -> value.toString().toLowerCase()).collect(Collectors.toSet());
        return driverWrapper -> browsers.isEmpty() || browsers.contains(driverWrapper.getCapability().getBrowserName());
    }

    public static Predicate<WebDriverWrapper<?>> filter(Capability... values) {
        Set<Capability> capabilities = Sets.newHashSet(values);
        return driverWrapper -> {
            return capabilities.isEmpty() || capabilities.stream().allMatch(capability -> {
                String currCap = (String) driverWrapper.getCapability().getCapability(capability.name());
                if (currCap == null) {
                    currCap = "";
                }
                return currCap.equalsIgnoreCase(capability.value());
            });
        };
    }

    public static Predicate<WebDriverWrapper<?>> filterFromSystemProperties() {
        Predicate<WebDriverWrapper<?>> filter = driverWrapper -> true;
        if (System.getProperty("browsers") != null) {
            Set<String> browsers = new HashSet<>(Splitter.on(',').trimResults().splitToList(System.getProperty("browsers")));
            filter = filter.and(driverWrapper -> browsers.contains(driverWrapper.getCapability().getBrowserName()));
        }
        return filter;
    }

}