package se.redmind.rmtest.selenium.example;

import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import se.redmind.rmtest.selenium.grid.*;
import se.redmind.rmtest.selenium.example.*;

@RunWith(Parallelized.class)
public class RMExampleMobile {

	private WebDriver tDriver;
	private final DriverNamingWrapper driverWrapper;
	private final String driverDescription;
	private String startUrl = TestParams.getBaseUrl();
	private RmMobileNav tMobNav;

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
		System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		Thread.sleep(500L);
		tMobNav.openTpi("Tjänster", "TPI™ – Test process improvement");

		tMobNav.assertPageTitle("TPI™ – Test process improvement");
		Thread.sleep(2000L);
		System.out.println("Page title is: " + tDriver.getTitle());

	}

	@Test
	public void management() throws Exception {
		//tDriver = driverWrapper.getDriver();
		//System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		Thread.sleep(500L);
		tMobNav.openManag("Tjänster", "Management");

		tMobNav.assertPageTitle("Management");
		Thread.sleep(2000L);
		System.out.println("Page title is: " + tDriver.getTitle());
	}

	@Test
	public void rekrytering() throws Exception {
		tDriver = driverWrapper.getDriver();
		System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		Thread.sleep(500L);
		tMobNav.openRyk("Tjänster", "Rekrytering");

		tMobNav.assertPageTitle("Rekrytering");
		Thread.sleep(2000L);
		System.out.println("Page title is: " + tDriver.getTitle());
	}

	@Test
	public void ClientAcademy() throws Exception {
		tDriver = driverWrapper.getDriver();
		System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		Thread.sleep(500L);
		tMobNav.openClAc("Tjänster", "Client Academy");

		tMobNav.assertPageTitle("Client Academy");
		Thread.sleep(2000L);
		System.out.println("Page title is: " + tDriver.getTitle());
	}

	@Test
	public void Konsulttjänster() throws Exception {
		tDriver = driverWrapper.getDriver();
		System.out.println("Driver:" + tDriver);

		tMobNav = new RmMobileNav(tDriver, startUrl);
		tMobNav.openMobileMenu();
		Thread.sleep(500L);
		tMobNav.openKTj("Tjänster", "Konsulttjänster", "Acceptance tester");

//		tMobNav.assertPageTitle("Client Academy");
		Thread.sleep(2000L);
		tDriver.quit();
	}
}