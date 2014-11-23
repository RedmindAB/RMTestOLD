package se.redmind.rmtest.selenium.grid;

import io.appium.java_client.AppiumDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;



/**
 * @author petter
 *
 */
public class DriverProvider {




	private static Boolean isInitialized = false;
	private static ArrayList <UrlCapContainer> urlCapList = new ArrayList<UrlCapContainer>();
	static ArrayList <DriverNamingWrapper> driverList = new ArrayList<DriverNamingWrapper>();
	
	
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

				if (capability.getCapability("description") != null) { 
					description = (String) capability.getCapability("description");
				} else if (nodeReq.getDescription() != null) {
					description = nodeReq.getDescription();
				} else {
					description = capability.getPlatform() + " " + capability.getBrowserName();

				}

				System.out.println("Description of driver is: " + description);
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
	 * @param filteredUrlCapList
	 */
	private static void startDrivers(ArrayList <UrlCapContainer> filteredUrlCapList){
		driverList = new ArrayList<DriverNamingWrapper>();
		DesiredCapabilities capability;
		URL driverUrl;
		String description;
		for (int i = 0; i < filteredUrlCapList.size(); i++) {
			capability = filteredUrlCapList.get(i).getCapability();
			driverUrl = filteredUrlCapList.get(i).getUrl();
			description = filteredUrlCapList.get(i).getDescription();

			try {

				WebDriver driver;
				if (capability.getCapability("rmDeviceType") == null) {
					driver = new RemoteWebDriver(driverUrl, capability);
					System.out.println("This is a RemoteWebDriver");
				} else {
//					driver = new SwipeableWebDriver(driverUrl, capability);
					driver = new AppiumDriver(driverUrl, capability);
					System.out.println("This is a AppiumDriver");
				}

				driverList.add(new DriverNamingWrapper(description, driver, capability, driverUrl));
				System.out.println("Started driver: " + description);

			} catch (UnreachableBrowserException e) {
				System.out.println("This driver seems to be nonresponsive: " +
						description + " ::: " + driverUrl.toString());
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	//	public static void cleanStaleDrivers() {
	//		for (int driverNr = 0; driverNr < driverList.size(); driverNr++) {
	//			try {
	//				System.out.println(driverList.get(driverNr).toString());
	//				driverList.get(driverNr).getDriver();
	//			} catch (Exception e) {
	//				// TODO: handle exception
	//				e.printStackTrace();
	//			}
	//
	//		}
	//	}

	/**
	 * 
	 */
	public static void stopDrivers() {

		for (int i = 0; i < driverList.size(); i++) {
			System.out.println("Closing driver: " + driverList.get(i).getDriverDescription());
			try {
				driverList.get(i).getDriver().quit();
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
		
		startDrivers(urlCapList);
		return driverList.toArray();
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
		startDrivers(filteredUrlCapList);
		return driverList.toArray();
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
			if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform2)) {
				filteredUrlCapList.add(urlCapList.get(i));
			}
		}
		startDrivers(filteredUrlCapList);
		return driverList.toArray();
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
					//                    System.out.println("Choosen driver: " + driverList.get(i));
					filteredUrlCapList.add(urlCapList.get(i));
				}
			}
		}
		//        System.out.println("Number of drivers: " + filteredDriverList.size());
		startDrivers(filteredUrlCapList);
		return driverList.toArray();
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
		startDrivers(filteredUrlCapList);
		return driverList.toArray();
	}
}
