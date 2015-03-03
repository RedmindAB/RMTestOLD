package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.steadystate.css.parser.Locatable;

import se.redmind.rmtest.selenium.framework.StackTraceInfo;
import se.redmind.rmtest.selenium.grid.*;
import se.redmind.rmtest.selenium.example.*;

@RunWith(Parallelized.class)
public class RMExample {


//	    private WebDriver tDriver;
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

		private void prepPage(WebDriver tDriver) throws Exception {
			tDriver = driverWrapper.getDriver();   
	    	System.out.println("Driver:" + tDriver);

	        tNavPage = new RMNav(tDriver, startUrl);
	        tMobNav = new RmMobileNav(tDriver, startUrl);
		}
	    
	    @Test
	    public void management() throws Exception {
	    	WebDriver tDriver = driverWrapper.getDriver();
	    	prepPage(tDriver);
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	//Andriod devices
	        	if (driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
	        		tMobNav.openMobileMenu();
	        		
	        		tMobNav.clickOnAndroidMenu("Tjänster", "Management");
	        		
		        	tMobNav.assertPageTitle("Management");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tMobNav.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	} else { // Mobile sites on desktop
	        		tMobNav.openMobileMenu();
		        	tMobNav.clickOnMobileMenu("Tjänster", "Management");
		    		
		        	tMobNav.assertPageTitle("Management");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	}
	        } else { //desktop
//	        	tDriver.manage().window().maximize() ;
	        	tNavPage.clickOnSubmenu("tjanster", "management");
	        	
	            tNavPage.assertPageTitle("Management");
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        }
	    }
	    
	    @Test
	    public void TPI() throws Exception {
	    	WebDriver tDriver = driverWrapper.getDriver();
	    	prepPage(tDriver);
	    	
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	if (driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
	        		tMobNav.openMobileMenu();
	        		
	        		tMobNav.clickOnAndroidMenu("Tjänster", "TPI™ – Test process improvement");
	        		
		        	tMobNav.assertPageTitle("TPI™ – Test process improvement");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tMobNav.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	} else { // Mobile sites on desktop
	        		tMobNav.openMobileMenu();
		        	tMobNav.clickOnMobileMenu("Tjänster", "TPI™ – Test process improvement");
		    		
		        	tMobNav.assertPageTitle("TPI™ – Test process improvement");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	}
	        } else { //Desktop 
	        	
	        	if (driverWrapper.getCapability().getBrowserName() == "safari") {
	        		//safari-code
	        		//Assume driver initialized properly.
	        		WebElement element = tDriver.findElement(By.id("Element id"));
	        		Locatable hoverItem = (Locatable) element;
	        		Mouse mouse = ((HasInputDevices) tDriver).getMouse();
	        		//mouse.mouseMove(hoverItem.getLocator());
	        	} else {
	        		tNavPage.clickOnSubmenu("tjanster", "tpi-test-process-improvement");
		            tNavPage.assertPageTitle("TPI™ – Test process improvement | Redmind");
		            
		            
		            System.out.println("Page title is: " + tDriver.getTitle());
		            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	}
	        }
	    }
	    
	    @Test
	    public void rekrytering() throws Exception {
	    	WebDriver tDriver = driverWrapper.getDriver();
	    	prepPage(tDriver);
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	if (driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
	        		tMobNav.openMobileMenu();
	        		
	        		tMobNav.clickOnAndroidMenu("Tjänster", "Rekrytering");
	        		
		        	tMobNav.assertPageTitle("Rekrytering");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tMobNav.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	} else { // Mobile sites on desktop
	        		tMobNav.openMobileMenu();
		        	tMobNav.clickOnMobileMenu("Tjänster", "Rekrytering");
		    		
		        	tMobNav.assertPageTitle("Rekrytering");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tMobNav.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	}
	        	
	        } else { //Desktop
	        	tNavPage.clickOnSubmenu("tjanster", "rekrytering");
	            tNavPage.assertPageTitle("Rekrytering");
	            
	            
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        }
	    }
	    
	    @Test
	    public void clientAcademy() throws Exception {
	    	WebDriver tDriver = driverWrapper.getDriver();
	    	prepPage(tDriver);
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	if (driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
	        		tMobNav.openMobileMenu();
	        		
	        		tMobNav.clickOnAndroidMenu("Tjänster", "Client Academy");
	        		
		        	tMobNav.assertPageTitle("Client Academy");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tMobNav.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	} else { // Mobile sites on desktop
	        		tMobNav.openMobileMenu();
		        	tMobNav.clickOnMobileMenu("Tjänster", "Client Academy");
		    		
		        	tMobNav.assertPageTitle("Client Academy");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tMobNav.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	}
	        } else { //Desktop
	        	tNavPage.clickOnSubmenu("tjanster", "client-academy");
	            tNavPage.assertPageTitle("Client Academy");
	          
	            
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        }
	    }
	    
	    @Test
	    public void konsulttjanster() throws Exception {
	    	WebDriver tDriver = driverWrapper.getDriver();
	    	prepPage(tDriver);
	        
	        //Mobile
	        if (tDriver.findElement(By.className("mobile-menu-wrapper")).isDisplayed()) {
	        	if (driverWrapper.getCapability().getPlatform() == Platform.ANDROID) {
	        		tMobNav.openMobileMenu();
	        		
	        		tMobNav.clickOnAndroidMenu("Tjänster", "Konsulttjänster","Acceptance tester");
	        		
		        	tMobNav.assertPageTitle("Acceptance tester");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tMobNav.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	} else { // Mobile sites on desktop
	        		tMobNav.openMobileMenu();
		        	tMobNav.clickOnMobileMenu("Tjänster", "Konsulttjänster", "Acceptance tester");
		    		
		        	tMobNav.assertPageTitle("Acceptance tester");	
		    		System.out.println("Page title is: " + tDriver.getTitle());
		    		tMobNav.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        	}
	        } else { //Desktop
	        	tNavPage.clickOnSubmenu("tjanster", "konsulttjanster");
	            assertTrue(tDriver.getTitle().startsWith("Konsulttjänster"));
	            
	            System.out.println("Page title is: " + tDriver.getTitle());
	            tNavPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + driverWrapper.getDescription().replace(" ", "-"));
	        }
	    }
}


	    