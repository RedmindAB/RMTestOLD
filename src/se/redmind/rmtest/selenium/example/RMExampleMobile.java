package se.redmind.rmtest.selenium.example;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parallelized.class)
public class RMExampleMobile {

    private static final Logger LOG = LoggerFactory.getLogger(RMExampleMobile.class);
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
        for(int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i],
                    wrapperList[i].toString()});
        }
        return returnList;
    }

    @Test
    public void tpi() throws Exception {
        tDriver = driverWrapper.getDriver();
        wait = new WebDriverWait(tDriver, 1);
        LOG.debug("Driver:" + tDriver);
        tMobNav = new RmMobileNav(tDriver, startUrl);
        tMobNav.openMobileMenu();
        tMobNav.driverWaitElementPresent(By.linkText("Tjänster"), 1);
        tMobNav.openTpi("Tjänster", "TPI™ – Test process improvement");
        tMobNav.assertPageTitle("TPI™ – Test process improvement");
        tMobNav.driverFluentWait(2);
        LOG.debug("Page title is: " + tDriver.getTitle());
    }
}