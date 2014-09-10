package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.redmind.rmtest.selenium.framework.StackTraceInfo;
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
	        return DriverProvider.getDrivers(Platform.MAC);

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

		private void prepPage() throws Exception {
			tDriver = driverWrapper.getDriver();   
	    	System.out.println("Driver:" + tDriver);

	        tNavPage = new RMNav(tDriver, startUrl);
	        tMobNav = new RmMobileNav(tDriver, startUrl);
		}
	    
	    @Test
	    public void management() throws Exception {
	    	prepPage();
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	tMobNav.openMobileMenu();
	        	
	        	tMobNav.driverFluentWait(1);
	        	
	    		tMobNav.openManag("Tjänster", "Management");
	    		tMobNav.assertPageTitle("Management");
	    		
	    		tMobNav.driverFluentWait(1);
	    		
	    		System.out.println("Page title is: " + tDriver.getTitle());
	    		tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        } else { //desktop
	        	tNavPage.clickOnSubmenu("tjanster", "management");
	            tNavPage.assertPageTitle("Management");
	            
	            tNavPage.driverFluentWait(1);
	            
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        }
	    }
	    
	    @Test
	    public void TPI() throws Exception {
	    	prepPage();
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	tMobNav.openMobileMenu();
	    		
	    		tMobNav.driverWaitElementPresent(By.linkText("Tjänster"), 1);

	    		tMobNav.openTpi("Tjänster", "TPI™ – Test process improvement");
	    		tMobNav.assertPageTitle("TPI™ – Test process improvement");
	    		tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        } else { //Desktop 
	        	tNavPage.clickOnSubmenu("tjanster", "tpi-test-process-improvement");
	            tNavPage.assertPageTitle("TPI™ – Test process improvement | Redmind");
	            
	            tNavPage.driverFluentWait(1);
	            
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        }
	    }
	    
	    @Test
	    public void rekrytering() throws Exception {
	    	prepPage();
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	    		tMobNav.openMobileMenu();
	    		tMobNav.driverWaitElementPresent(By.linkText("Tjänster"), 1);
	    		
	    		tMobNav.openRyk("Tjänster", "Rekrytering");
	    		tMobNav.assertPageTitle("Rekrytering");
	    		tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        } else { //Desktop
	        	tNavPage.clickOnSubmenu("tjanster", "rekrytering");
	            tNavPage.assertPageTitle("Rekrytering");
	            
	            tNavPage.driverFluentWait(1);
	            
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        }
	    }
	    
	    @Test
	    public void clientAcademy() throws Exception {
	    	prepPage();
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	    		tMobNav.openMobileMenu();

	    		tMobNav.driverWaitElementPresent(By.linkText("Tjänster"), 1);
	    		
	    		tMobNav.openClAc("Tjänster", "Client Academy");
	    		tMobNav.assertPageTitle("Client Academy");
	    		tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        } else { //Desktop
	        	tNavPage.clickOnSubmenu("tjanster", "client-academy");
	            tNavPage.assertPageTitle("Client Academy");
	            
	            tNavPage.driverFluentWait(1);
	            
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        }
	    }
	    
	    @Test
	    public void konsulttjanster() throws Exception {
	    	prepPage();
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	tMobNav.openMobileMenu();

	    		tMobNav.driverWaitElementPresent(By.linkText("Tjänster"), 1);
	    		tMobNav.openKTj("Tjänster", "Konsulttjänster", "Acceptance tester");

	    		tMobNav.assertPageTitle("Acceptance tester");
	    		tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        } else { //Desktop
	        	tNavPage.clickOnSubmenu("tjanster", "konsulttjanster");
	            assertTrue(tDriver.getTitle().startsWith("Konsulttjänster"));
	            
	            tNavPage.driverFluentWait(1);
	            
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDriverDescription().replace(" ", "-"));
	        }
	    }
}


	    