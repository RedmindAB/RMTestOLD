package se.redmind.rmtest.comaround.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.redmind.rmtest.selenium.grid.DriverProvider;
//import se.aftonbladet.abtest.tests.mobile.menu.AbMobileNavTest;




@RunWith(Suite.class)
@Suite.SuiteClasses( {ComaroundTest1.class})
public class ComaroundTests {
    @BeforeClass
    public static void beforeAllTests(){
    	
    }

    
    @AfterClass
    public static void afterAllTests(){
        DriverProvider.stopDrivers();
    }
}
