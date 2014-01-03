package se.remind.rmtest.selenium.tests.desktop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.Platform;

import java.util.HashMap;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import se.remind.rmtest.selenium.grid.DriverNamingWrapper;
import se.remind.rmtest.selenium.grid.DriverProvider;



@RunWith(JUnitParamsRunner.class)
public class GoogleExampleDesktop {


    private Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.MAC, "safari");
    }


    @Test
    @Parameters(method = "getDrivers")
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
    @Parameters(method = "getDrivers")
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