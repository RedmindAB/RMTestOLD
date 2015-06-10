package se.redmind.rmtest.selenium.grid;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.net.URL;

import org.junit.Assume;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;

public class DriverNamingWrapper {

	private URL url;
	private DesiredCapabilities capability;
	String description;
	WebDriver driver;
	boolean imAFailure;


	public DriverNamingWrapper(URL url, DesiredCapabilities capability, String description) {
		this.url = url;
		this.capability = capability;
		this.description = description;
		this.imAFailure = false;
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

	public void ignoreAtNoConnectivityById(String url, String id) {		
		ignoreAtNoConnectivityTo(url, By.id(id));
	}

	public void ignoreAtNoConnectivityByClass(String url, String className) {		
		ignoreAtNoConnectivityTo(url, By.className(className));
	}

	public void ignoreAtNoConnectivityByXpath(String url, String xpath) {		
		ignoreAtNoConnectivityTo(url, By.xpath(xpath));
	}

	public void ignoreAtNoConnectivityTo(String url, By by) {		
		if (!imAFailure) {
			try {
				getDriver().get(url);
				driverWaitElementPresent(by, 10);
			} catch (NoSuchElementException|TimeoutException e) {
				this.imAFailure = true;
				Assume.assumeTrue("This driver doesn't seem to have connectivity to: " + url,false);

			}	
		}
	}


	/**
	 * @param pBy
	 * @param timeoutInSeconds
	 */
	public void driverWaitElementPresent(By pBy, int timeoutInSeconds) {
		new WebDriverWait(getDriver(), timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(pBy));
	}

	/**
	 * @param filteredUrlCapList
	 */
	public WebDriver startDriver(){
		if (this.driver == null) {

			int maxRetryAttempts = 5;

			if (this.imAFailure) {
				Assume.assumeTrue("Since driver didn't start after  " + maxRetryAttempts + " attempts, it probably wont start now ",false);
				return this.driver;
			} else {

				int retryAttempts = 1;

				while (retryAttempts <= maxRetryAttempts) {
					try {

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
						System.out.println("Having trouble starting webdriver for device: " + this.description);
						System.out.println("Attempt " + retryAttempts + " of " + maxRetryAttempts);
						e.printStackTrace();
						retryAttempts++;
						continue;
					} catch (SessionNotCreatedException e) {
						System.out.println("Having trouble starting webdriver for device: " + this.description);
						System.out.println("Attempt " + retryAttempts + " of " + maxRetryAttempts);
						e.printStackTrace();
						retryAttempts++;
						continue;
					} catch (Exception e) {
						System.out.println("Having trouble starting webdriver for device: " + this.description);
						System.out.println("Attempt " + retryAttempts + " of " + maxRetryAttempts);
						e.printStackTrace();
						retryAttempts++;
						continue;
					}
					return this.driver;
				}
				this.imAFailure=true;
				Assume.assumeTrue("Driver failed to start properly after " + (retryAttempts - 1) + " attempts",false);
				return this.driver;
			}
		} else {
			return this.driver;
		}
	}


	@Override
	public String toString() {
		return description;
	}
}