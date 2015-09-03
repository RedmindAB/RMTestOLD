package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.framework.TestParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.rmtest.selenium.grid.RmAllDevice;

@RunWith(Parallelized.class)
public class GoogleExample extends RmAllDevice{

//	    private final DriverNamingWrapper driverWrapper;
//	    private final String driverDescription;
//	    private final RMReportScreenshot rmrScreenshot;
//		private HTMLPage navPage;

	    public GoogleExample(final DriverNamingWrapper driverWrapper, final String driverDescription) {
	        super(driverWrapper, TestParams.getBaseUrl());
	        driverWrapper.addDriverConfig(new TestConfig());
	    }

	    @AfterClass
	    public static void afterTest(){
//	    	DriverProvider.stopDrivers();
	    }

	    
	    @Before
	    public void before(){
	    	this.webDriver = driverNamingWrapper.startDriver();
	    }

	/*
	 * @rmTest
	 */
    @Test
    public void testGoogle() throws Exception {
    	HTMLPage navPage = new HTMLPage(driverNamingWrapper.getDriver());
        
        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name

        System.out.println("Page title is: " + navPage.getTitle());
        
        assertTrue(navPage.getTitle().startsWith("Goo"));
        
        
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot(null);
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("first");
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("after");
        System.out.println("Done!");   
        
    }
    
    
    @Test
    public void testGoogle2() throws Exception {
    	HTMLPage navPage = new HTMLPage(driverNamingWrapper.getDriver());
        
    	navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name

        System.out.println("Page title is: " + navPage.getTitle());
        
        assertTrue(navPage.getTitle().startsWith("Goo"));
        
        
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("");
        System.out.println("Done!");        
        
    }

}