package test.java.se.redmind.rmtest.selenium.example;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;

import se.redmind.rmtest.selenium.example.GoogleExample;
//import se.aftonbladet.abtest.tests.mobile.menu.AbMobileNavTest;
import se.redmind.rmtest.selenium.grid.DriverProvider;




@RunWith(Suite.class)
@Suite.SuiteClasses( {GoogleExample.class})
public class CreateLogTests {
    @BeforeClass
    public static void beforeAllTests(){
        
    }

    
    @AfterClass
    public static void afterAllTests(){
       DriverProvider.stopDrivers();
    }
}
