package se.redmind.rmtest.selenium.tests.desktop;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;



@RunWith(Parallelized.class)
public class GoogleExampleDesktop {


	   private WebDriver tDriver;
	    private final DriverNamingWrapper driverWrapper;
	    private final String driverDescription;

	    public GoogleExampleDesktop(final DriverNamingWrapper driverWrapper, final String driverDescription) {
	        this.driverWrapper = driverWrapper;
	        this.driverDescription = driverDescription;
	    }
	    
	    private static Object[] getDrivers() {
	        return DriverProvider.getDrivers(Platform.ANDROID);

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
    public void testGoogle(DriverNamingWrapper driverWrapper) throws Exception {
        WebDriver driver = driverWrapper.getDriver();
                
        // And now use this to visit Google
        driver.get("http://www.google.com");
        
        // Find the text input element by its name
//        WebElement element = driver.findElement(By.name("q"));
        System.out.println("Page title is: " + driver.getTitle());
        
        assertTrue(driver.getTitle().startsWith("Goo"));
//        assertFalse(driver.getTitle().startsWith("Goo"));
        
        
    }
    @Ignore
    @Test
    public void testAOS(DriverNamingWrapper driverWrapper) throws Exception {
        WebDriver driver = driverWrapper.getDriver();
        // And now use this to visit Google
        driver.get("http://www.aos.se");

        // Find the text input element by its name
//        WebElement element = driver.findElement(By.name("q"));
        System.out.println("Page title is: " + driver.getTitle());
        assertTrue(driver.getTitle().startsWith("Allt om Stockholm"));
    }
    

}