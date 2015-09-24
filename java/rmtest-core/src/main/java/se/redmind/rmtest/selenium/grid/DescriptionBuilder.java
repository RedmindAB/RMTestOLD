package se.redmind.rmtest.selenium.grid;

import com.google.common.base.Strings;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.redmind.rmtest.selenium.framework.DeviceDescription;

public class DescriptionBuilder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param capabilities
     * @return
     */
    public String buildDescriptionFromCapabilities(DesiredCapabilities capabilities) {
        String os = getOS(capabilities);
        String osVer = getOsVersion(capabilities);
        String device = getDevice(capabilities);
        String browser = getBrowser(capabilities);
        String browserVersion = "UNKNOWN";

        String description = new DeviceDescription(os, osVer, device, browser, browserVersion).getDeviceDescription();
        logger.debug("Description of driver is: " + description);
        return description;
    }

    private String getBrowser(DesiredCapabilities capability) {
        String browser;
        if (isCapabilitySet("browserName", capability)) {
            browser = getSafeCapability("browserName", capability);
        } else if (isCapabilitySet("appPackage", capability)) {
            browser = getSafeCapability("appPackage", capability);
        } else {
            browser = "UNKNOWN";
        }
        return browser;
    }

    private String getDevice(DesiredCapabilities capability) {
        String device;
        if (isCapabilitySet("deviceName", capability)) {
            device = getSafeCapability("deviceName", capability);
        } else {
            device = "UNKNOWN";
        }
        return device;
    }

    private String getOsVersion(DesiredCapabilities capability) {
        String osVer;
        if (isCapabilitySet("platformVersion", capability)) {
            osVer = getSafeCapability("platformVersion", capability);
        } else {
            osVer = "UNKNOWN";
        }
        return osVer;
    }

    private String getOS(DesiredCapabilities capability) {
        String os;
        if (isCapabilitySet("rmOsName", capability)) {
            os = getSafeCapability("rmOsName", capability);
        } else if (isCapabilitySet("platformName", capability)) {
            os = getSafeCapability("platformName", capability);
        } else if (isCapabilitySet("osname", capability)) {
            os = getSafeCapability("osname", capability);
        } else if (isCapabilitySet("platform", capability)) {
            os = getSafeCapability("platform", capability);
        } else {
            os = "UNKNOWN";
        }
        return os;
    }

    private Boolean isCapabilitySet(String capName, DesiredCapabilities currentCapability) {
        return !Strings.isNullOrEmpty(getSafeCapability(capName, currentCapability));
    }

    private String getSafeCapability(String capName, DesiredCapabilities currentCapability) {
        return (String) currentCapability.getCapability(capName);
    }

}