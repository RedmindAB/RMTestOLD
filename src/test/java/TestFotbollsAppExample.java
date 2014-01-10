package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.aftonbladet.abtest.navigation.fotboll.FotbollNav;
import se.aftonbladet.abtest.navigation.mobil.AbMobileNav;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.TestParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.rmtest.selenium.grid.SwipeableWebDriver;


@RunWith(Parallelized.class)
public class TestFotbollsAppExample {
    private WebDriver tDriver;
    private FotbollNav appNav;
    private String startUrl = TestParams.getBaseUrl();

    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;
    
    public TestFotbollsAppExample(final DriverNamingWrapper driverWrapper, final String driverDescription) {
       this.driverWrapper = driverWrapper;
       this.driverDescription = driverDescription;
    }
    
    private static Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.MAC);
        
    }
    
    
    @Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
        Object[] wrapperList = getDrivers();
        for (int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
        }
        return returnList;
    }

    
    @Test
    public void testFotboll() throws Exception {
        
        WebDriver driver = driverWrapper.getDriver();
        appNav = new FotbollNav(driver);
        appNav.initialStartNoAction();
        appNav.openMenu();
        appNav.closeMenu();
//        driver.quit();
    }



}