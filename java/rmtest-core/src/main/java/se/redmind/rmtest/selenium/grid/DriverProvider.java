package se.redmind.rmtest.selenium.grid;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.config.Configuration;
import se.redmind.rmtest.config.GridConfiguration;
import se.redmind.rmtest.config.LocalConfiguration;
import se.redmind.rmtest.selenium.framework.Browser;

/**
 * @author petter
 */
public class DriverProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverProvider.class);
    private static ArrayList<DriverNamingWrapper> urlCapList = new ArrayList<>();
    private static ArrayList<DriverNamingWrapper> allDrivers = new ArrayList<>();

    private static void updateDrivers() {
        Configuration config = Configuration.current();
        urlCapList = new ArrayList<>();
        if (config.runner instanceof LocalConfiguration) {
            loadLocalDrivers(config.runner.as(LocalConfiguration.class));
        } else if (config.runner instanceof GridConfiguration) {
            loadGridDrivers(config.runner.as(GridConfiguration.class));
        } else {
            throw new UnsupportedOperationException("unsupported configuration type " + config.runner);
        }
    }

    private static void loadGridDrivers(GridConfiguration config) {
        HubNodesStatus nodeInfo = new HubNodesStatus(config.hubIp, GridConstants.hubPort);
        ArrayList<RegistrationRequest> nodeList = nodeInfo.getNodesAsRegReqs();

        nodeList.forEach(nodeReq -> {
            nodeReq.getCapabilities().stream()
                .map(capability -> new DesiredCapabilities(capability))
                .forEach(currentCapability -> {
                    try {
                        String description = DescriptionBuilder.buildDescriptionFromCapabilities(currentCapability);
                        URL driverUrl = new URL("http://" + nodeReq.getConfigAsString("host") + ":" + nodeReq.getConfigAsString("port") + "/wd/hub");
                        DriverNamingWrapper driver = new DriverNamingWrapper(driverUrl, currentCapability, description);
                        urlCapList.add(driver);
                        allDrivers.add(driver);
                    } catch (MalformedURLException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                });
        });
    }

    private static void loadLocalDrivers(LocalConfiguration config) {
        for (Browser browser : Browser.values()) {
            if (browser == Browser.PhantomJS && config.usePhantomJS) {
                DriverNamingWrapper driver = new DriverNamingWrapper(browser, browser.toString());
                urlCapList.add(driver);
                allDrivers.add(driver);
            }
            if (browser == Browser.Chrome && config.useChrome) {
                DriverNamingWrapper driver = new DriverNamingWrapper(browser, browser.toString());
                urlCapList.add(driver);
                allDrivers.add(driver);
            }
            if (browser == Browser.Firefox && config.useFirefox) {
                DriverNamingWrapper driver = new DriverNamingWrapper(browser, browser.toString());
                urlCapList.add(driver);
                allDrivers.add(driver);
            }
        }
    }

    /**
     *
     */
    public static void stopDrivers() {
        allDrivers.stream().forEach(allDriver -> {
            LOGGER.info("Closing driver: " + allDriver.getDescription());
            try {
                if (allDriver.getDriver() != null) {
                    allDriver.getDriver().quit();
                }
            } catch (SessionNotFoundException e) {
                LOGGER.error("For some reason a session was gone while quitting", e);
            } catch (WebDriverException e) {
                LOGGER.error("Crached webdriver, continue to closing drivers");
            }
        });
        allDrivers = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public synchronized static Object[] getDrivers() {
        updateDrivers();
        return urlCapList.toArray();
    }

    /**
     * @param pPlatform
     * @return
     */
    public synchronized static Object[] getDrivers(Platform pPlatform) {
        updateDrivers();
        return urlCapList.stream()
            .filter(urlCapList1 -> urlCapList1.getCapability().getPlatform().is(pPlatform))
            .collect(Collectors.toList()).toArray();
    }

    /**
     * @param pPlatform1
     * @param pPlatform2
     * @return
     */
    public synchronized static Object[] getDrivers(Platform pPlatform1, Platform pPlatform2) {
        updateDrivers();
        return urlCapList.stream()
            .filter(urlCapList1 -> urlCapList1.getCapability().getPlatform().is(pPlatform1) || urlCapList1.getCapability().getPlatform().is(pPlatform2))
            .collect(Collectors.toList()).toArray();
    }

    /**
     * @param pPlatform
     * @param pBrowserName
     * @return
     */
    public synchronized static Object[] getDrivers(Platform pPlatform, String pBrowserName) {
        updateDrivers();
        return urlCapList.stream()
            .filter(urlCapList1 -> urlCapList1.getCapability().getPlatform().is(pPlatform) || urlCapList1.getCapability().getBrowserName().contains(pBrowserName))
            .collect(Collectors.toList()).toArray();
    }

    /**
     * get drivers that match capability key,value pair
     *
     * @param capKey
     * @param capValue
     * @return
     */
    public synchronized static Object[] getDrivers(String capKey, String capValue) {
        updateDrivers();
        ArrayList<DriverNamingWrapper> filteredUrlCapList = new ArrayList<>();
        String currCap;
        for (DriverNamingWrapper urlCapList1 : urlCapList) {
            currCap = (String) urlCapList1.getCapability().getCapability(capKey);
            if (currCap == null) {
                currCap = "";
            }
            if (currCap.equalsIgnoreCase(capValue)) {
                filteredUrlCapList.add(urlCapList1);
            }
        }
        return filteredUrlCapList.toArray();
    }
}
