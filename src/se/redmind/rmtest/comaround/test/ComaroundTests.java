package se.redmind.rmtest.comaround.test;

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
@Suite.SuiteClasses( {ComaroundTest1.class})
public class ComaroundTests {
    @BeforeClass
    public static void beforeAllTests(){
        
    }

    
    @AfterClass
    public static void afterAllTests(){
        DriverProviderNew.stopDrivers();
    }
}
