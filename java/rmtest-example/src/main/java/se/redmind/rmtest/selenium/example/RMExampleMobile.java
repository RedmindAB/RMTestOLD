package se.redmind.rmtest.selenium.example;

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

import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

@RunWith(Parallelized.class)
public class RMExampleMobile {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WebDriver tDriver;
    public final DriverWrapper<?> driverWrapper;
    public final String driverDescription;
    public WebDriverWait wait;
    private final String startUrl = TestParams.getBaseUrl();
    private RmMobileNav tMobNav;
    File scr;

    public RMExampleMobile(DriverWrapper<?> driverWrapper, final String driverDescription) {
        this.driverWrapper = driverWrapper;
        this.driverDescription = driverDescription;
    }

    private static Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.MAC, "firefox");

    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<>();
        Object[] wrapperList = getDrivers();
        for (Object wrapperList1 : wrapperList) {
            returnList.add(new Object[]{wrapperList1, wrapperList1.toString()});
        }

        return returnList;
    }

    @Test
    public void tpi() throws Exception {
        tDriver = driverWrapper.getDriver();
        wait = new WebDriverWait(tDriver, 1);
        logger.info("Driver:" + tDriver);
        tMobNav = new RmMobileNav(tDriver, startUrl);
        tMobNav.openMobileMenu();

        tMobNav.driverWaitElementPresent(By.linkText("Tjänster"), 1);
        tMobNav.openTpi("Tjänster", "TPI™ – Test process improvement");
        tMobNav.assertPageTitle("TPI™ – Test process improvement");
        tMobNav.driverFluentWait(2);
        logger.info("Page title is: " + tDriver.getTitle());
    }
}
