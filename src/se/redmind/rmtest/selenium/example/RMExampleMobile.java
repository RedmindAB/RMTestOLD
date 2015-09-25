package se.redmind.rmtest.selenium.example;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import se.redmind.rmtest.selenium.grid.*;

@RunWith(Parallelized.class)
public class RMExampleMobile {

	private WebDriver tDriver;
	public final DriverNamingWrapper driverWrapper;
	public final String driverDescription;
	public WebDriverWait wait;
	private String startUrl = TestParams.getBaseUrl();
	private RmMobileNav tMobNav;
	File scr;

	public RMExampleMobile(final DriverNamingWrapper driverWrapper,
			final String driverDescription) {
		this.driverWrapper = driverWrapper;
		this.driverDescription = driverDescription;
	}

	private static Object[] getDrivers() {
		return DriverProvider.getDrivers(Platform.MAC, "firefox");

	}

	@Parameterized.Parameters(name = "{1}")
	public static Collection<Object[]> drivers() {
		ArrayList<Object[]> returnList = new ArrayList<Object[]>();
		Object[] wrapperList = getDrivers();
		for (int i = 0; i < wrapperList.length; i++) {
			returnList.add(new Object[] { wrapperList[i],
					wrapperList[i].toString() });
		}

		return returnList;
	}

	@Test
	public void tpi() throws Exception {
		tDriver = driverWrapper.getDriver();
		wait = new WebDriverWait(tDriver, 1);
		System.out.println("Driver:" + tDriver);
		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		
		tMobNav.driverWaitElementPresent(By.linkText("Tjänster"), 1);
		// Thread.sleep(500L);
		tMobNav.openTpi("Tjänster", "TPI™ – Test process improvement");
		tMobNav.assertPageTitle("TPI™ – Test process improvement");
		tMobNav.driverFluentWait(2);
		// Thread.sleep(2000L);
		System.out.println("Page title is: " + tDriver.getTitle());
	}
	/*
	@Test
	public void management() throws Exception {
		tDriver = driverWrapper.getDriver();
		wait = new WebDriverWait(tDriver, 1);
		System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		wait.until(ExpectedConditions.elementToBeClickable(By
				.linkText("Tjänster")));
		// Thread.sleep(500L);
		tMobNav.openManag("Tjänster", "Management");
		tMobNav.assertPageTitle("Management");
		// Thread.sleep(2000L);
		System.out.println("Page title is: " + tDriver.getTitle());
		File scr = ((TakesScreenshot) tDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File(
				"C://Users/Samoyl3000/Desktop/Screenshot/scr02.png"));
	}

	@Test
	public void rekrytering() throws Exception {
		tDriver = driverWrapper.getDriver();
		wait = new WebDriverWait(tDriver, 1);
		System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		wait.until(ExpectedConditions.elementToBeClickable(By
				.linkText("Tjänster")));
		// Thread.sleep(500L);
		tMobNav.openRyk("Tjänster", "Rekrytering");
		tMobNav.assertPageTitle("Rekrytering");
		// Thread.sleep(2000L);
		System.out.println("Page title is: " + tDriver.getTitle());
		File scr = ((TakesScreenshot) tDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File(
				"C://Users/Samoyl3000/Desktop/Screenshot/scr03.png"));
	}

	@Test
	public void ClientAcademy() throws Exception {
		tDriver = driverWrapper.getDriver();
		wait = new WebDriverWait(tDriver, 1);
		System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		wait.until(ExpectedConditions.elementToBeClickable(By
				.linkText("Tjänster")));
		// Thread.sleep(500L);
		tMobNav.openClAc("Tjänster", "Client Academy");

		tMobNav.assertPageTitle("Client Academy");
		// Thread.sleep(2000L);
		System.out.println("Page title is: " + tDriver.getTitle());
		File scr = ((TakesScreenshot) tDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File(
				"C://Users/Samoyl3000/Desktop/Screenshot/scr04.png"));
	}

	@Test
	public void Konsulttjänster() throws Exception {
		tDriver = driverWrapper.getDriver();
		wait = new WebDriverWait(tDriver, 1);
		System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		wait.until(ExpectedConditions.elementToBeClickable(By
				.linkText("Tjänster")));
		// Thread.sleep(500L);
		tMobNav.openKTj("Tjänster", "Konsulttjänster", "Acceptance tester");

		tMobNav.assertPageTitle("Acceptance tester");
		File scr = ((TakesScreenshot) tDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File(
				"C://Users/Samoyl3000/Desktop/Screenshot/scr05.png"));
		Thread.sleep(2000L);
		tDriver.quit();
	}
	*/
}