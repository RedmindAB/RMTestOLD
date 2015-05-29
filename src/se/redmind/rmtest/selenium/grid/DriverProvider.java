package se.redmind.rmtest.selenium.grid;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionNotFoundException;

import se.redmind.rmtest.selenium.framework.Browser;
import se.redmind.rmtest.selenium.framework.DeviceDescription;



/**
 * @author petter
 *
 */
public class DriverProvider {





	private static ArrayList <DriverNamingWrapper> urlCapList = new ArrayList<DriverNamingWrapper>();
	private static ArrayList <DriverNamingWrapper> allDrivers = new ArrayList<DriverNamingWrapper>();
	private static DesiredCapabilities currentCapability;
	
	
	/**
	 * 
	 */
	private static void updateDrivers() {
		RmConfig config = new RmConfig();
		urlCapList = new ArrayList<DriverNamingWrapper>();
		if (!RmConfig.runOnGrid()) {
			loadLocalDrivers();
			return;
		}
		HubNodesStatus nodeInfo = new HubNodesStatus(config.getHubIp(), GridConstatants.hubPort);
		ArrayList <RegistrationRequest> nodeList = nodeInfo.getNodesAsRegReqs();

		RegistrationRequest nodeReq;
		String description;

		for (int j = 0; j < nodeList.size(); j++) { 
			nodeReq = nodeList.get(j); 
			for (int i = 0; i < nodeReq.getCapabilities().size(); i++) {
				currentCapability = new DesiredCapabilities(nodeReq.getCapabilities().get(i));

				description = buildDescriptionFromCapabilities(currentCapability);
				URL driverUrl;
				try {
					driverUrl = new URL("http://" + nodeReq.getConfigAsString("host") + ":" + nodeReq.getConfigAsString("port") + "/wd/hub");
					DriverNamingWrapper driver = new DriverNamingWrapper(driverUrl, currentCapability, description);
					urlCapList.add(driver);
					allDrivers.add(driver);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	private static void loadLocalDrivers() {
		for (int i = 0; i < Browser.values().length; i++) {
			Browser browser = Browser.values()[i];
			if (browser == Browser.PhantomJS || browser == Browser.Chrome) {
				if (!RmConfig.usePhantomJS()) {
					continue;
				}
				else if (!RmConfig.useChrome()) {
					continue;
				}
			}
			DriverNamingWrapper driver = new DriverNamingWrapper(browser, browser.toString());
			urlCapList.add(driver);
			allDrivers.add(driver);
		}
	}

	private static String getSafeCapability(String capName) {
		String capValue = (String) currentCapability.getCapability(capName);
		return capValue;
	}
	
	private static Boolean isCapabilitySet(String capName) {
		String capValue = getSafeCapability(capName);
		if (capValue == null || capValue.equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @param capabilities
	 * @return
	 */
	private static String buildDescriptionFromCapabilities(
			DesiredCapabilities capabilities) {
		String description;
		
		String os;
		if (isCapabilitySet("rmOsName")) { 
			os = getSafeCapability("rmOsName");
		} else if (isCapabilitySet("platformName")) { 
			os = getSafeCapability("platformName");
		} else if (isCapabilitySet("osname")) { 
			os = getSafeCapability("osname");
		} else if (isCapabilitySet("platform")) {
			os = getSafeCapability("platform");
		} else {
			os = "UNKNOWN";
		}
		
		
		String osVer;
		if (isCapabilitySet("platformVersion")){
			osVer = getSafeCapability("platformVersion");
		} else {
			osVer = "UNKNOWN";
		}
		
		String device;
		if (isCapabilitySet("deviceName")) {
			device = getSafeCapability("deviceName"); 
		} else {
			device = "UNKNOWN";
		}
		
		String browser = null;
		if (isCapabilitySet("browserName")) {
			browser = getSafeCapability("browserName"); 
		} 
		else if(isCapabilitySet("appPackage")){
			browser = getSafeCapability("appPackage");
		}
		else {
			browser = "UNKNOWN";
		}
		
		String browserVersion = "UNKNOWN";
		
		description = new DeviceDescription(os, osVer, device, browser, browserVersion).getDeviceDescription();

		System.out.println("Description of driver is: " + description);
		return description;
	}

	/**
	 * 
	 */
	public static void stopDrivers() {

		for (int i = 0; i < allDrivers.size(); i++) {
			System.out.println("Closing driver: " + allDrivers.get(i).getDescription());
			try {
				if (allDrivers.get(i).getDriver() != null) {
					allDrivers.get(i).getDriver().quit();
				}
				
			} catch (SessionNotFoundException e) {
				System.out.println("For some reason a session was gone while quitting");
				System.out.println(e);
			}
			
			

		}
		allDrivers = new ArrayList<DriverNamingWrapper>();
	}

	
	/**
	 * 
	 * @return 
	 */
	public synchronized static Object[] getDrivers() {
		updateDrivers();
		
//		startDrivers(urlCapList);
		return urlCapList.toArray();
	}

	
	/**
	 * @param pPlatform
	 * @return
	 */
	public synchronized static Object[] getDrivers(Platform pPlatform) {
		updateDrivers();
		ArrayList <DriverNamingWrapper> filteredUrlCapList = new ArrayList<DriverNamingWrapper>();
		for (int i = 0; i < urlCapList.size(); i++) {
			if (urlCapList.get(i).getCapability().getPlatform().is(pPlatform)) {
				filteredUrlCapList.add(urlCapList.get(i));
			}
		}
		return filteredUrlCapList.toArray();
	}


	/**
	 * @param pPlatform1
	 * @param pPlatform2
	 * @return
	 */
	public synchronized static Object[] getDrivers(Platform pPlatform1, Platform pPlatform2) {
		updateDrivers();
		ArrayList <DriverNamingWrapper> filteredUrlCapList = new ArrayList<DriverNamingWrapper>();
		for (int i = 0; i < urlCapList.size(); i++) {
			if (urlCapList.get(i).getCapability().getPlatform().is(pPlatform1)) {
				filteredUrlCapList.add(urlCapList.get(i));
			}
			if (urlCapList.get(i).getCapability().getPlatform().is(pPlatform2)) {
				filteredUrlCapList.add(urlCapList.get(i));
			}
		}
		return filteredUrlCapList.toArray();
	}


	/**
	 * @param pPlatform
	 * @param pBrowserName
	 * @return
	 */
	public synchronized static Object[] getDrivers(Platform pPlatform, String pBrowserName) {
		updateDrivers();
		ArrayList <DriverNamingWrapper> filteredUrlCapList = new ArrayList<DriverNamingWrapper>();
		for (int i = 0; i < urlCapList.size(); i++) {
			if (urlCapList.get(i).getCapability().getPlatform().is(pPlatform)) {
				if (urlCapList.get(i).getCapability().getBrowserName().contains(pBrowserName)) {
					filteredUrlCapList.add(urlCapList.get(i));
				}
			}
		}		
		return filteredUrlCapList.toArray();
	}


	/**
	 * get drivers that match capability key,value pair
	 * @param capKey
	 * @param capValue
	 * @return
	 */
	public synchronized static Object[] getDrivers(String capKey, String capValue) {
		updateDrivers();
		ArrayList <DriverNamingWrapper> filteredUrlCapList = new ArrayList<DriverNamingWrapper>();
		String currCap;
		for (int i = 0; i < urlCapList.size(); i++) {
			currCap = (String) urlCapList.get(i).getCapability().getCapability(capKey);
			if (currCap == null) {
				currCap = "";
			}
			if (currCap.equalsIgnoreCase(capValue)) {
				filteredUrlCapList.add(urlCapList.get(i));
			}
		}
		return filteredUrlCapList.toArray();
	}
}
