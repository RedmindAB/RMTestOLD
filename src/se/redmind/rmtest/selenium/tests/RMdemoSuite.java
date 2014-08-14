package se.redmind.rmtest.selenium.tests;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;


import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.tests.mobile.RMDemo;



@RunWith(Suite.class)
@Suite.SuiteClasses( {RMDemo.class})
public class RMdemoSuite {
    @BeforeClass
    public static void beforeAllTests(){
        DriverProvider.startDrivers();
    }

    
    @AfterClass
    public static void afterAllTests(){
        DriverProvider.stopDrivers();
    }
}