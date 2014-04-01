using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections;
using Newtonsoft.Json.Linq;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;

namespace RMTest
{
	public class DriverProvider 
	{
		private static List<DriverNamingWrapper> driverList = new List<DriverNamingWrapper>();
		private static Boolean isInitialized = false;
    
		public static void startDrivers()
		{
			RmConfig config = new RmConfig();
			String hubHost = config.getHubIp();
			int hubPort = 4444;
			//HubNodesStatus nodeInfo = new HubNodesStatus(config.getHubIp(), GridConstatants.hubPort);
			HubNodesStatus nodeInfo = new HubNodesStatus(hubHost, hubPort);
			JArray nodeList = nodeInfo.getNodesAsJson();

			if (isInitialized) 
			{
				Console.WriteLine("Already started drivers.");
			}
			else 
			{
				//JObject nodeReq;
				String description;
				JArray capabilities;
				JObject configuration;
				foreach (JObject nodeReq in nodeList.Children<JObject>())
				{
					configuration = JObject.Parse(nodeReq.GetValue("configuration").ToString());
					capabilities = JArray.Parse(nodeReq.GetValue("capabilities").ToString());
					DesiredCapabilities capability;
					foreach (JObject jsonCapablility in capabilities.Children<JObject>())
					{
					//for (int i = 0; i < nodeReq.getCapabilities().size(); i++) {
						capability = new DesiredCapabilities();
						foreach (JProperty p in jsonCapablility.Properties())
						{
							  capability.SetCapability(p.Name, p.Value.ToString());  
						}
	//                    System.out.println("DESCRIPTION OF DEVICE: " + capability.getCapability("description") + nodeReq.getDescription());
					
						if (capability.GetCapability("description") != null) 
						{ 
                    		description = (String) capability.GetCapability("description");
						} 
						else if (nodeReq.GetValue("description") != null) 
						{
                    		description = nodeReq.GetValue("description").ToString();
						} 
						else 
						{
                    		description = capability.Platform + " " + capability.BrowserName;

						}
                    
						Console.WriteLine("Description of driver is: " + description);
                    


						try {
							Uri driverUrl = new Uri(configuration.GetValue("remoteHost") + "/wd/hub");
							IWebDriver driver;
							if (capability.GetCapability("app-package") == null) {
								driver = new RemoteWebDriver(driverUrl, capability);
							} else {
								driver = new RemoteWebDriver(driverUrl, capability);
								//driver = new SwipeableWebDriver(driverUrl, capability);
							}
                        
							driverList.Add(new DriverNamingWrapper(description, driver, capability, driverUrl));
							Console.WriteLine("Started driver: " + description);
              
    
						} catch (Exception e) {
							// TODO: handle exception
							Console.WriteLine(e.Message);
						}
                    
					}
				}
				isInitialized = true;
			}
		}
    
		//public static void cleanStaleDrivers() {
		//	for (int driverNr = 0; driverNr < driverList.size(); driverNr++) {
		//		try {
		//			System.out.println(driverList.get(driverNr).toString());
		//			driverList.get(driverNr).getDriver();
		//		} catch (Exception e) {
		//			// TODO: handle exception
		//			e.printStackTrace();
		//		}

		//	}
		//}

		public static void stopDrivers()
		{
			//DriverNamingWrapper DriveWrap;
			for (int i = 0; i < driverList.Count; i++)
			{
				//DriverNamingWrapper DriveWrap = driverList[i];
				Console.WriteLine("Closing driver: " + driverList[i].getDriverDescription());
				driverList[i].getDriver().Quit();

			}
		}

		/**
		 * 
		 * @return 
		 */
		public static void getDrivers() {
			startDrivers();
			for (int i = 0; i < driverList.Count; i++)
			{
				Console.WriteLine(driverList[i].toString());
			}
		
		}
    
	//	/**
	//	 * 
	//	 * @return 
	//	 */
	//	public synchronized static Object[] getDrivers(Platform pPlatform) {
	//		startDrivers();
	//		ArrayList <DriverNamingWrapper> filteredDriverList = new ArrayList<DriverNamingWrapper>();
	//		for (int i = 0; i < driverList.size(); i++) {
	//			if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform)) {
	//				filteredDriverList.add(driverList.get(i));
	//			}
	//		}
	//		return filteredDriverList.toArray();
	//	}
    
	//	/**
	//	 * 
	//	 * @return 
	//	 */
	//	public synchronized static Object[] getDrivers(Platform pPlatform1, Platform pPlatform2) {
	//		startDrivers();
	//		ArrayList <DriverNamingWrapper> filteredDriverList = new ArrayList<DriverNamingWrapper>();
	//		for (int i = 0; i < driverList.size(); i++) {
	//			if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform1)) {
	//				filteredDriverList.add(driverList.get(i));
	//			}
	//			if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform2)) {
	//				filteredDriverList.add(driverList.get(i));
	//			}
	//		}
	//		return filteredDriverList.toArray();
	//	}
    
	//	/**
	//	 * 
	//	 * @return 
	//	 */
	//	public synchronized static Object[] getDrivers(Platform pPlatform, String pBrowserName) {
	//		startDrivers();
	//		ArrayList <DriverNamingWrapper> filteredDriverList = new ArrayList<DriverNamingWrapper>();
	//		for (int i = 0; i < driverList.size(); i++) {
	//			if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform)) {
	//				if (driverList.get(i).getCapabilities().getBrowserName().contains(pBrowserName)) {
	////                    System.out.println("Choosen driver: " + driverList.get(i));
	//					filteredDriverList.add(driverList.get(i));
	//				}
	//			}
	//		}
	////        System.out.println("Number of drivers: " + filteredDriverList.size());
	//		return filteredDriverList.toArray();
	//	}
    
  
	//	/**
	//	 * get drivers that match capability key,value pair
	//	 * @param capKey
	//	 * @param capValue
	//	 * @return
	//	 */
	//	public synchronized static Object[] getDrivers(String capKey, String capValue) {
	//		startDrivers();
	//		ArrayList <DriverNamingWrapper> filteredDriverList = new ArrayList<DriverNamingWrapper>();
	//		String currCap;
	//		for (int i = 0; i < driverList.size(); i++) {
	//			currCap = (String) driverList.get(i).getCapabilities().getCapability(capKey);
	//			if (currCap == null) {
	//				currCap = "";
	//			}
	//			if (currCap.equalsIgnoreCase(capValue)) {
	//				filteredDriverList.add(driverList.get(i));
	//			}
	//		}
	//		return filteredDriverList.toArray();
	//	}
    
    
	}
}
