package test.java.se.redmind.rmtest.selenium.example;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.BeforeClass;

import se.redmind.rmtest.selenium.example.CopyOfGoogleExample;
import se.redmind.rmtest.selenium.example.GoogleExample;
import se.redmind.rmtest.selenium.framework.RmSuite;
import se.redmind.rmtest.selenium.grid.DriverProvider;




@RunWith(RmSuite.class)
@SuiteClasses( {GoogleExample.class, CopyOfGoogleExample.class})
public class GoogleTestsRMR {
	
    @BeforeClass
    public static void beforeAllTests(){
        
    }

    
    @AfterClass
    public static void afterAllTests(){
        DriverProvider.stopDrivers();
    }
}
