package se.redmind.rmtest.selenium.tests.mobile;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.redmind.rmtest.selenium.grid.DriverProvider;

@RunWith(Suite.class)
@Suite.SuiteClasses( {GoogleExampleAndroid.class})
public class MobileTestsExample {
    @BeforeClass
    public static void beforeAllTests(){
        DriverProvider.startDrivers();
    }

    
    @AfterClass
    public static void afterAllTests(){
        DriverProvider.stopDrivers();
    }
}