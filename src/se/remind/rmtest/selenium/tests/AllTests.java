package se.remind.rmtest.selenium.tests;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;

import se.remind.rmtest.selenium.grid.DriverProvider;
import se.remind.rmtest.selenium.tests.mobile.menu.AbMobileNavTest;



@RunWith(Suite.class)
@Suite.SuiteClasses( {AbMobileNavTest.class})
public class AllTests {
    @BeforeClass
    public static void beforeAllTests(){
        DriverProvider.startDrivers();
    }

    
    @AfterClass
    public static void afterAllTests(){
//        DriverProvider.stopDrivers();
    }
}