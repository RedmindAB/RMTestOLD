package se.redmind.rmtest.selenium.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.config.Configuration;

/**
 * @author petter
 */
public class DriverProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverProvider.class);
    private static final ArrayList<DriverWrapper<?>> CURRENT_DRIVERS = new ArrayList<>();
    private static final ArrayList<DriverWrapper<?>> ALL_DRIVERS = new ArrayList<>();

    private static void updateDrivers() {
        List<DriverWrapper<?>> drivers = new ArrayList<>();
        Configuration.current().drivers.forEach(driverConfiguration -> drivers.addAll(driverConfiguration.wrappers()));
        CURRENT_DRIVERS.clear();
        CURRENT_DRIVERS.addAll(drivers);
        ALL_DRIVERS.addAll(drivers);
    }

    public static void stopDrivers() {
        ALL_DRIVERS.stream().forEach(driverInstance -> {
            try {
                driverInstance.stopDriver();
            } catch (SessionNotFoundException e) {
                LOGGER.error("For some reason a session was gone while quitting", e);
            } catch (WebDriverException e) {
                LOGGER.error("Crached webdriver, continue to closing drivers");
            }
        });
        ALL_DRIVERS.clear();
    }

    public static Collection<Object[]> getDriversAsParameters() {
        return Arrays.asList(getDrivers()).stream().map(obj -> new Object[]{obj}).collect(Collectors.toList());
    }

    public synchronized static Object[] getDrivers() {
        updateDrivers();
        return CURRENT_DRIVERS.toArray();
    }

    /**
     * @param pPlatform
     * @return
     */
    public synchronized static Object[] getDrivers(Platform pPlatform) {
        updateDrivers();
        return CURRENT_DRIVERS.stream()
            .filter(driver -> driver.getCapability().getPlatform().is(pPlatform))
            .collect(Collectors.toList()).toArray();
    }

    /**
     * @param pPlatform1
     * @param pPlatform2
     * @return
     */
    public synchronized static Object[] getDrivers(Platform pPlatform1, Platform pPlatform2) {
        updateDrivers();
        return CURRENT_DRIVERS.stream()
            .filter(driver -> driver.getCapability().getPlatform().is(pPlatform1) || driver.getCapability().getPlatform().is(pPlatform2))
            .collect(Collectors.toList()).toArray();
    }

    /**
     * @param pPlatform
     * @param pBrowserName
     * @return
     */
    public synchronized static Object[] getDrivers(Platform pPlatform, String pBrowserName) {
        updateDrivers();
        return CURRENT_DRIVERS.stream()
            .filter(driver -> driver.getCapability().getPlatform().is(pPlatform) || driver.getCapability().getBrowserName().contains(pBrowserName))
            .collect(Collectors.toList()).toArray();
    }

    /**
     * get drivers that match getCapability key,value pair
     *
     * @param capKey
     * @param capValue
     * @return
     */
    public synchronized static Object[] getDrivers(String capKey, String capValue) {
        updateDrivers();
        ArrayList<DriverWrapper<?>> filteredUrlCapList = new ArrayList<>();
        String currCap;
        for (DriverWrapper<?> driver : CURRENT_DRIVERS) {
            currCap = (String) driver.getCapability().getCapability(capKey);
            if (currCap == null) {
                currCap = "";
            }
            if (currCap.equalsIgnoreCase(capValue)) {
                filteredUrlCapList.add(driver);
            }
        }
        return filteredUrlCapList.toArray();
    }
}
