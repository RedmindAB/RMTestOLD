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

	public WebDriver getDriver() {
		return this.driver;
	}
	

	/**
	 * @param filteredUrlCapList
	 */
	public WebDriver startDriver(){
		if (this.driver == null) {
			
		
		int maxRetryAttempts = 5;
		int retryAttempts = 0;
		while (retryAttempts < maxRetryAttempts) {
			System.out.println("Attempt: " + retryAttempts);

			try {

//				WebDriver driver;
				if (capability.getCapability("rmDeviceType") == null) {
					this.driver = new RemoteWebDriver(url, capability);
					System.out.println("This is a RemoteWebDriver");
				} else {

					if ("Android".equalsIgnoreCase((String) capability.getCapability("platformName")) ) {
						this.driver = new AndroidDriver(url, capability);
					} else {
						this.driver = new IOSDriver(url, capability);
					}

					System.out.println("This is a AppiumDriver");
				}

				System.out.println("Started driver: " + description);

			} catch (UnreachableBrowserException e) {
//				System.out.println("This driver seems to be nonresponsive: " +
//						description + " ::: " + url.toString());
				e.printStackTrace();
				retryAttempts++;
			} catch (SessionNotCreatedException e) {
				e.printStackTrace();
				retryAttempts++;
			} catch (Exception e) {
				e.printStackTrace();
				retryAttempts++;
			}
			return this.driver;
		}
		assertTrue("Driver failed to start properly after " + retryAttempts + " attempts",false);
		return this.driver;
		} else {
			return this.driver;
		}
	}


	@Override
	public String toString() {
		return description;
	}
}