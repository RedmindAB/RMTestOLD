package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.assertTrue;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steadystate.css.parser.Locatable;

@RunWith(Parallelized.class)
public class RMExample {

    private static final Logger LOG = LoggerFactory.getLogger(RMExample.class);
    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;
    private String startUrl = TestParams.getBaseUrl();
    private RMNav tNavPage;
    private RmMobileNav tMobNav;

    public RMExample(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        this.driverWrapper = driverWrapper;
        this.driverDescription = driverDescription;
    }

    private static Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.MAC);
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
        Object[] wrapperList = getDrivers();
        for(int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
        }
        return returnList;
    }

    private void prepPage(WebDriver tDriver) throws Exception {
        tDriver = driverWrapper.startDriver();
        LOG.debug("Driver:" + tDriver);
        tNavPage = new RMNav(tDriver, startUrl);
        tMobNav = new RmMobileNav(tDriver, startUrl);
    }

    @Test
    public void management() throws Exception {
        WebDriver tDriver = driverWrapper.getDriver();
        prepPage(tDriver);
        //Mobile
        if(tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
            //Andriod devices
            if(driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
                tMobNav.openMobileMenu();
                tMobNav.clickOnAndroidMenu("Tjänster", "Management");
                tMobNav.assertPageTitle("Management");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
            else { // Mobile sites on desktop
                tMobNav.openMobileMenu();
                tMobNav.clickOnMobileMenu("Tjänster", "Management");
                tMobNav.assertPageTitle("Management");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
        }
        else { //desktop
//	        	tDriver.manage().window().maximize() ;
            tNavPage.clickOnSubmenu("tjanster", "management");
            tNavPage.assertPageTitle("Management");
            LOG.debug("Page title is: " + tDriver.getTitle());
        }
    }

    @Test
    public void TPI() throws Exception {
        WebDriver tDriver = driverWrapper.getDriver();
        prepPage(tDriver);
        //Mobile
        if(tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
            if(driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
                tMobNav.openMobileMenu();
                tMobNav.clickOnAndroidMenu("Tjänster", "TPI™ – Test process improvement");
                tMobNav.assertPageTitle("TPI™ – Test process improvement");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
            else { // Mobile sites on desktop
                tMobNav.openMobileMenu();
                tMobNav.clickOnMobileMenu("Tjänster", "TPI™ – Test process improvement");
                tMobNav.assertPageTitle("TPI™ – Test process improvement");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
        }
        else { //Desktop
            if(driverWrapper.getCapability().getBrowserName() == "safari") {
                //safari-code
                //Assume driver initialized properly.
                WebElement element = tDriver.findElement(By.id("Element id"));
                Locatable hoverItem = (Locatable) element;
                Mouse mouse = ((HasInputDevices) tDriver).getMouse();
                //mouse.mouseMove(hoverItem.getLocator());
            }
            else {
                tNavPage.clickOnSubmenu("tjanster", "tpi-test-process-improvement");
                tNavPage.assertPageTitle("TPI™ – Test process improvement | Redmind");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
        }
    }

    @Test
    public void rekrytering() throws Exception {
        WebDriver tDriver = driverWrapper.getDriver();
        prepPage(tDriver);
        //Mobile
        if(tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
            if(driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
                tMobNav.openMobileMenu();
                tMobNav.clickOnAndroidMenu("Tjänster", "Rekrytering");
                tMobNav.assertPageTitle("Rekrytering");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
            else { // Mobile sites on desktop
                tMobNav.openMobileMenu();
                tMobNav.clickOnMobileMenu("Tjänster", "Rekrytering");
                tMobNav.assertPageTitle("Rekrytering");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
        }
        else { //Desktop
            tNavPage.clickOnSubmenu("tjanster", "rekrytering");
            tNavPage.assertPageTitle("Rekrytering");
            LOG.debug("Page title is: " + tDriver.getTitle());
        }
    }

    @Test
    public void clientAcademy() throws Exception {
        WebDriver tDriver = driverWrapper.getDriver();
        prepPage(tDriver);
        //Mobile
        if(tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
            if(driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
                tMobNav.openMobileMenu();
                tMobNav.clickOnAndroidMenu("Tjänster", "Client Academy");
                tMobNav.assertPageTitle("Client Academy");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
            else { // Mobile sites on desktop
                tMobNav.openMobileMenu();
                tMobNav.clickOnMobileMenu("Tjänster", "Client Academy");
                tMobNav.assertPageTitle("Client Academy");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
        }
        else { //Desktop
            tNavPage.clickOnSubmenu("tjanster", "client-academy");
            tNavPage.assertPageTitle("Client Academy");
            LOG.debug("Page title is: " + tDriver.getTitle());
        }
    }

    @Test
    public void konsulttjanster() throws Exception {
        WebDriver tDriver = driverWrapper.getDriver();
        prepPage(tDriver);
        //Mobile
        if(tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
            if(driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
                tMobNav.openMobileMenu();
                tMobNav.clickOnAndroidMenu("Tjänster", "Konsulttjänster", "Acceptance tester");
                tMobNav.assertPageTitle("Acceptance tester");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
            else { // Mobile sites on desktop
                tMobNav.openMobileMenu();
                tMobNav.clickOnMobileMenu("Tjänster", "Konsulttjänster", "Acceptance tester");
                tMobNav.assertPageTitle("Acceptance tester");
                LOG.debug("Page title is: " + tDriver.getTitle());
            }
        }
        else { //Desktop
            tNavPage.clickOnSubmenu("tjanster", "konsulttjanster");
            assertTrue(tDriver.getTitle().startsWith("Konsulttjänster"));
            LOG.debug("Page title is: " + tDriver.getTitle());
        }
    }
}


	    