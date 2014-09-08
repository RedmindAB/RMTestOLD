package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

//import se.redmind.rmtest.selenium.framework.TestParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

import se.redmind.rmtest.selenium.example.TestParams;


@RunWith(Parallelized.class)
public class RMExampleDesktop {


	    private WebDriver tDriver;
	    private final DriverNamingWrapper driverWrapper;
	    private final String driverDescription;
	    private String startUrl = TestParams.getBaseUrl();
	    private RMNav tNavPage;
	    private RmMobileNav tMobNav;

	    public RMExampleDesktop(final DriverNamingWrapper driverWrapper, final String driverDescription) {
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
    /*
    @Test
    public void TPI() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "tpi-test-process-improvement");
        
        tNavPage.assertPageTitle("TPI™ – Test process improvement | Redmind");
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void rekrytering() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "rekrytering");
        
        tNavPage.assertPageTitle("Rekrytering");
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void clientAcademy() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "client-academy");
        
        tNavPage.assertPageTitle("Client Academy");
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void konsulttjanster() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "konsulttjanster");
        
        assertTrue(tDriver.getTitle().startsWith("Konsulttjänster"));
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void konsulttjansterAcceptanceTester() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "konsulttjanster", "acceptance-tester");
        
        assertTrue(tDriver.getTitle().startsWith("Acceptance tester"));
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void konsulttjansterAgileTester() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "konsulttjanster", "agile-tester");
        
        assertTrue(tDriver.getTitle().startsWith("Agile tester"));
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void konsulttjansterAutomationSpecialist() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "konsulttjanster", "automation-specialist");
        
        assertTrue(tDriver.getTitle().startsWith("Automation specialist"));
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void konsulttjansterHeadOfQA() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "konsulttjanster", "head-of-qa");
        
        assertTrue(tDriver.getTitle().startsWith("Head of QA"));
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void konsulttjansterSystemTester() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "konsulttjanster", "system-tester");
        
        assertTrue(tDriver.getTitle().startsWith("System tester"));
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void konsulttjansterTestManager() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "konsulttjanster", "test-manager");
        
        assertTrue(tDriver.getTitle().startsWith("Test manager"));
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    
    @Test
    public void konsulttjansterTestStrategist() throws Exception {
    	tDriver = driverWrapper.getDriver();   
    	System.out.println("Driver:" + tDriver);

        tNavPage = new RMNav(tDriver, startUrl);
        
        tNavPage.clickOnSubmenu("tjanster", "konsulttjanster", "test-strategist");
        
        assertTrue(tDriver.getTitle().startsWith("Test strategist"));
        Thread.sleep(1000L);
        System.out.println("Page title is: " + tDriver.getTitle());
    }
    */
}
