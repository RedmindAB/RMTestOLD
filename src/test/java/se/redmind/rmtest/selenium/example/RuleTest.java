package test.java.se.redmind.rmtest.selenium.example;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;

import se.redmind.rmtest.selenium.example.PetterTest;
import se.redmind.rmtest.selenium.grid.DriverProvider;




@RunWith(Suite.class)
@Suite.SuiteClasses( {PetterTest.class})
public class RuleTest {
    @BeforeClass
    public static void beforeAllTests(){
        
    }

    
    @AfterClass
    public static void afterAllTests(){
        DriverProvider.stopDrivers();
    }
}
