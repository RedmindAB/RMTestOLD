package se.redmind.rmtest.selenium.grid;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;



public class DriverProvider {
    private static ArrayList <DriverNamingWrapper> driverList = new ArrayList<DriverNamingWrapper>();
    private static Boolean isInitialized = false;
    public static void startDrivers(){
        HubNodesStatus nodeInfo = new HubNodesStatus(RMTestConfig.getHubHost(), GridConstatants.hubPort);
        ArrayList <RegistrationRequest> nodeList = nodeInfo.getNodesAsRegReqs();

        if (isInitialized) {
            System.out.println("Already started drivers.");
        } else {
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
                    
                    try {
                        URL driverUrl = new URL("http://" + nodeReq.getConfigAsString("host") + ":" + nodeReq.getConfigAsString("port") + "/wd/hub");
                        WebDriver driver;
                        if (capability.getCapability("app-package") == null) {
                            driver = new RemoteWebDriver(driverUrl, capability);
                        } else {
                            driver = new SwipeableWebDriver(driverUrl, capability);
                        }
                        
                        driverList.add(new DriverNamingWrapper(description, driver, capability, driverUrl));
                        System.out.println("Started driver: " + description);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (UnreachableBrowserException e) {
                        System.out.println("This driver seems to be nonresponsive: " +
                                    description + " " +
                                    nodeReq.getConfigAsString("host") + ":" +
                                    nodeReq.getConfigAsString("port"));
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    
                }
            }
            isInitialized = true;
        }
    }
    
    public static void cleanStaleDrivers() {
        for (int driverNr = 0; driverNr < driverList.size(); driverNr++) {
            try {
                System.out.println(driverList.get(driverNr).toString());
                driverList.get(driverNr).getDriver();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }

    public static void stopDrivers() {

        for (int i = 0; i < driverList.size(); i++) {
            System.out.println("Closing driver: " + driverList.get(i).getDriverDescription());
            driverList.get(i).getDriver().quit();

        }
    }

    /**
     * 
     * @return 
     */
    public synchronized static Object[] getDrivers() {
        startDrivers();
        return driverList.toArray();
    }
    
    /**
     * 
     * @return 
     */
    public synchronized static Object[] getDrivers(Platform pPlatform) {
        startDrivers();
        ArrayList <DriverNamingWrapper> filteredDriverList = new ArrayList<DriverNamingWrapper>();
        for (int i = 0; i < driverList.size(); i++) {
            if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform)) {
                filteredDriverList.add(driverList.get(i));
            }
        }
        return filteredDriverList.toArray();
    }
    
    /**
     * 
     * @return 
     */
    public synchronized static Object[] getDrivers(Platform pPlatform1, Platform pPlatform2) {
        startDrivers();
        ArrayList <DriverNamingWrapper> filteredDriverList = new ArrayList<DriverNamingWrapper>();
        for (int i = 0; i < driverList.size(); i++) {
            if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform1)) {
                filteredDriverList.add(driverList.get(i));
            }
            if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform2)) {
                filteredDriverList.add(driverList.get(i));
            }
        }
        return filteredDriverList.toArray();
    }
    
    /**
     * 
     * @return 
     */
    public synchronized static Object[] getDrivers(Platform pPlatform, String pBrowserName) {
        startDrivers();
        ArrayList <DriverNamingWrapper> filteredDriverList = new ArrayList<DriverNamingWrapper>();
        for (int i = 0; i < driverList.size(); i++) {
            if (driverList.get(i).getCapabilities().getPlatform().is(pPlatform)) {
                if (driverList.get(i).getCapabilities().getBrowserName().contains(pBrowserName)) {
//                    System.out.println("Choosen driver: " + driverList.get(i));
                    filteredDriverList.add(driverList.get(i));
                }
            }
        }
//        System.out.println("Number of drivers: " + filteredDriverList.size());
        return filteredDriverList.toArray();
    }
    
  
    /**
     * get drivers that match capability key,value pair
     * @param capKey
     * @param capValue
     * @return
     */
    public synchronized static Object[] getDrivers(String capKey, String capValue) {
        startDrivers();
        ArrayList <DriverNamingWrapper> filteredDriverList = new ArrayList<DriverNamingWrapper>();
        String currCap;
        for (int i = 0; i < driverList.size(); i++) {
        	currCap = (String) driverList.get(i).getCapabilities().getCapability(capKey);
        	if (currCap == null) {
				currCap = "";
			}
            if (currCap.equalsIgnoreCase(capValue)) {
                filteredDriverList.add(driverList.get(i));
            }
        }
        return filteredDriverList.toArray();
    }
    
    
}
