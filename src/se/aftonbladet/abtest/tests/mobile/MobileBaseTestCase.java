package se.aftonbladet.abtest.tests.mobile;

import org.junit.Before;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 */
public abstract class MobileBaseTestCase {

	private WebDriver driver;


	@Before
	public void setup() {
		// setup the remote web driver instance?
		// String driverName = System.getProperty("se.aftonbladet.driver");
		String driverName = "android";
		System.out.println("dat driver name: " + driverName);
		this.driver = makeDriver(driverName);
	}

	public WebDriver makeDriver(String name) {
		if("chrome".equalsIgnoreCase(name)) {
			return new ChromeDriver();
		} else if("android".equalsIgnoreCase(name)) {
//			return new AndroidDriver();
		}
		return new FirefoxDriver();
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
}
