package se.redmind.rmtest.selenium.example;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;

import se.redmind.rmtest.selenium.grid.*;
import se.redmind.rmtest.selenium.example.*;

@RunWith(Parallelized.class)
public class RMExample {


	    private WebDriver tDriver;
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
	        return DriverProvider.getDrivers(Platform.MAC, "chrome");

	    }

	    @Parameterized.Parameters(name = "{1}")
	    public static Collection<Object[]> drivers() {
	        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
	        Object[] wrapperList = getDrivers();
	        for (int i = 0; i < wrapperList.length; i++) {
	            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
	        }

	        return returnList;
	   
	    }

	    @Test
	    public void management() throws Exception {
	    	tDriver = driverWrapper.getDriver();   
	    	System.out.println("Driver:" + tDriver);

	        tNavPage = new RMNav(tDriver, startUrl);
	        tMobNav = new RmMobileNav(tDriver, startUrl);
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	tMobNav.openMobileMenu();
	    		Thread.sleep(500L);
	    		tMobNav.openManag("Tjänster", "Management");

	    		tMobNav.assertPageTitle("Management");
	    		Thread.sleep(2000L);
	    		System.out.println("Page title is: " + tDriver.getTitle());
	        } else {//desktop
	        	tNavPage.clickOnSubmenu("tjanster", "management");
	            
	            tNavPage.assertPageTitle("Management");
	            Thread.sleep(1000L);
	            System.out.println("Page title is: " + tDriver.getTitle());
	        }
	    }
}


	    