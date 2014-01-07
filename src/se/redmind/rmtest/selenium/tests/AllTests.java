package se.redmind.rmtest.selenium.tests;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;

import se.aftonbladet.abtest.tests.mobile.menu.AbMobileNavTest;
import se.redmind.rmtest.selenium.grid.DriverProvider;



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