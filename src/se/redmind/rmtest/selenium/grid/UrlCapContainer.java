package se.redmind.rmtest.selenium.grid;

import static org.junit.Assert.assertTrue;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.net.URL;

import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class UrlCapContainer {

	private URL url;
	private DesiredCapabilities capability;
	String description;
	WebDriver driver;
	
	
	public UrlCapContainer(URL url, DesiredCapabilities capability, String description) {
		this.url = url;
		this.capability = capability;
		this.description = description;
		
	}
	
	public String getDescription() {
		return description;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public DesiredCapabilities getCapability() {
		return capability;
	}
	
	
	
	/**
	 * @param filteredUrlCapList
	 */
	public WebDriver startDriver(){
		

			try {

				WebDriver driver;
				if (capability.getCapability("rmDeviceType") == null) {
					this.driver = new RemoteWebDriver(url, capability);
					System.out.println("This is a RemoteWebDriver");
				} else {
//					driver = new SwipeableWebDriver(driverUrl, capability);
					try {
						if ("Android".equalsIgnoreCase((String) capability.getCapability("platformName")) ) {
							this.driver = new AndroidDriver(url, capability);
						} else {
							this.driver = new IOSDriver(url, capability);
						}
//						this.driver = new AppiumDriver(url, capability);
						
					} catch (SessionNotCreatedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						assertTrue("Driver failed to start properly",false);
					}
					System.out.println("This is a AppiumDriver");
				}

//				driverList.add(new DriverNamingWrapper(description, driver, capability, driverUrl));
				System.out.println("Started driver: " + description);

			} catch (UnreachableBrowserException e) {
				System.out.println("This driver seems to be nonresponsive: " +
						description + " ::: " + url.toString());
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return this.driver;
		
	}

	
    @Override
    public String toString() {
        return description;
    }
}