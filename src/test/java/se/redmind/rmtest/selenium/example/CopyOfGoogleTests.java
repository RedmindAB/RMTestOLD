package test.java.se.redmind.rmtest.selenium.example;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;

import se.redmind.rmtest.selenium.example.CopyOfGoogleExample;
import se.redmind.rmtest.selenium.example.GoogleExample;
//import se.aftonbladet.abtest.tests.mobile.menu.AbMobileNavTest;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.DriverProviderNew;




@RunWith(Suite.class)
@Suite.SuiteClasses( {CopyOfGoogleExample.class})
public class CopyOfGoogleTests {
    @BeforeClass
    public static void beforeAllTests(){
    	DriverProviderNew.clearAndFetchDrivers();
    	DriverProviderNew.addDrivers("deviceId", "LGD855aa0e605f");
    	DriverProviderNew.addDrivers();
    	System.out.println("BEFORECLASS!!");
    }

    
    @AfterClass
    public static void afterAllTests(){
        DriverProvider.stopDrivers();
    }
}