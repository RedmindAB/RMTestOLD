package se.redmind.rmtest.selenium.grid;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionNotFoundException;

import se.redmind.rmtest.selenium.framework.DeviceDescription;



/**
 * @author petter
 *
 */
public class DriverProvider {





	private static ArrayList <UrlCapContainer> urlCapList = new ArrayList<UrlCapContainer>();
	
	
	/**
	 * 
	 */
	private static void updateDrivers() {
		RmConfig config = new RmConfig();
		HubNodesStatus nodeInfo = new HubNodesStatus(config.getHubIp(), GridConstatants.hubPort);
		ArrayList <RegistrationRequest> nodeList = nodeInfo.getNodesAsRegReqs();
		urlCapList = new ArrayList<UrlCapContainer>();

		RegistrationRequest nodeReq;
		String description;

		for (int j = 0; j < nodeList.size(); j++) { 
			nodeReq = nodeList.get(j); 
			for (int i = 0; i < nodeReq.getCapabilities().size(); i++) {
				DesiredCapabilities capability = new DesiredCapabilities(nodeReq.getCapabilities().get(i));
				//                    System.out.println("DESCRIPTION OF DEVICE: " + capability.getCapability("description") + nodeReq.getDescription());

				description = buildDescriptionFromCapabilities(capability);
				URL driverUrl;
				try {
					driverUrl = new URL("http://" + nodeReq.getConfigAsString("host") + ":" + nodeReq.getConfigAsString("port") + "/wd/hub");
					urlCapList.add(new UrlCapContainer(driverUrl, capability, description));

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	/**
	 * @param capability
	 * @return
	 */
	private static String buildDescriptionFromCapabilities(
			DesiredCapabilities capability) {
		String description;
		
		String os;
		if (capability.getCapability("rmOsName") != null) { 
			os = (String) capability.getCapability("rmOsName");
		} else if (capability.getCapability("platformName") != null) { 
			os = (String) capability.getCapability("platformName");
		} else if (capability.getCapability("osname") != null) { 
			os = (String) capability.getCapability("osname");
		} else  {
			os = (String) capability.getCapability("platform");
		}
		
		
		String osVer;
		if (capability.getCapability("platformVersion") != null){
			osVer = (String) capability.getCapability("platformVersion");
		} else {
			osVer = "UNKNOWN";
		}
		
		String device;
		if (capability.getCapability("deviceName") != null) {
			device = (String) capability.getCapability("deviceName"); 
		} else {
			device = "UNKNOWN";
		}
		
		String browser;
		if (capability.getCapability("browserName") != null) {
			browser = (String) capability.getCapability("browserName"); 
		} else {
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

		for (int i = 0; i < urlCapList.size(); i++) {
			System.out.println("Closing driver: " + urlCapList.get(i).getDescription());
			try {
				urlCapList.get(i).getDriver().quit();
			} catch (SessionNotFoundException e) {
				System.out.println("For some reason a session was gone while quitting");
				System.out.println(e);
			}
			
			

		}
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
		ArrayList <UrlCapContainer> filteredUrlCapList = new ArrayList<UrlCapContainer>();
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
		ArrayList <UrlCapContainer> filteredUrlCapList = new ArrayList<UrlCapContainer>();
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
		ArrayList <UrlCapContainer> filteredUrlCapList = new ArrayList<UrlCapContainer>();
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
		ArrayList <UrlCapContainer> filteredUrlCapList = new ArrayList<UrlCapContainer>();
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
