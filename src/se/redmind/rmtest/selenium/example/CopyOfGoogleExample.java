package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.StackTraceInfo;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.rmtest.selenium.grid.UrlCapContainer;



@RunWith(Parallelized.class)
public class CopyOfGoogleExample {


	   private WebDriver tDriver;
	    private final UrlCapContainer urlContainer;
	    private final String driverDescription;
		private HTMLPage navPage;

	    public CopyOfGoogleExample(final UrlCapContainer driverWrapper, final String driverDescription) {
	        this.urlContainer = driverWrapper;
	        this.driverDescription = driverDescription;
	    }
	    
	    private static Object[] getDrivers() {
//	        return DriverProvider.getDrivers("rmDeviceType", "mobile");
//	    	return DriverProvider.getDrivers(Platform.ANDROID);
	    	return DriverProvider.getDriversNew();

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

//	    @AfterClass
//	    public static void afterAllTests(){
//	    	navPage.getDriver().quit();
//	    }
	    
    @Test
    public void testGoogle() throws Exception {
//        WebDriver driver = urlContainer.startDriver();
    	
        navPage = new HTMLPage(urlContainer.startDriver());
        
        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name

        System.out.println("Page title is: " + navPage.getTitle());
        
        assertTrue(navPage.getTitle().startsWith("Goo"));
        
        
        navPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + urlContainer.getDescription().replace(" ", "-"));
        System.out.println("Done!");
       
       navPage.getDriver().quit();

        
        
    }
    @Test
    public void testGoogle2() throws Exception {
//        WebDriver driver = urlContainer.startDriver();
    	
        navPage = new HTMLPage(urlContainer.startDriver());
        
        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name

        System.out.println("Page title is: " + navPage.getTitle());
        
        assertTrue(navPage.getTitle().startsWith("Goo"));
        
        
        navPage.takeScreenshot(StackTraceInfo.getCurrentMethodName() + "_" + urlContainer.getDescription().replace(" ", "-"));
        System.out.println("Done!");
       
       navPage.getDriver().quit();

        
        
    }

}