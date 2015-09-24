package se.redmind.rmtest.selenium.grid;

import org.openqa.selenium.remote.DesiredCapabilities;

import se.redmind.rmtest.selenium.framework.DeviceDescription;

public class DescriptionBuilder {

	
	
	
	/**
	 * @param capabilities
	 * @return
	 */
	public String buildDescriptionFromCapabilities(DesiredCapabilities capabilities) {
		String description;
		
		String os;
		os = getOS(capabilities);
		
		String osVer;
		osVer = getOsVersion(capabilities);
		
		String device;
		device = getDevice(capabilities);
		
		String browser;
		browser = getBrowser(capabilities);
		
		String browserVersion = "UNKNOWN";
		
		description = new DeviceDescription(os, osVer, device, browser, browserVersion).getDeviceDescription();

		System.out.println("Description of driver is: " + description);
		return description;
	}

	private String getBrowser(DesiredCapabilities capability) {
		String browser;
		if (isCapabilitySet("browserName", capability)) {
			browser = getSafeCapability("browserName", capability); 
		} 
		else if(isCapabilitySet("appPackage", capability)){
			browser = getSafeCapability("appPackage", capability);
		}
		else {
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
		if (isCapabilitySet("platformVersion", capability)){
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
		String capValue = getSafeCapability(capName, currentCapability);
		if (capValue == null || capValue.equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	private String getSafeCapability(String capName, DesiredCapabilities currentCapability) {
		String capValue = (String) currentCapability.getCapability(capName);
		return capValue;
	}
	
}
