package se.redmind.rmtest.selenium.tests.desktop;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.redmind.rmtest.selenium.grid.DriverProvider;

@RunWith(Suite.class)
@Suite.SuiteClasses( {GoogleExampleDesktop.class, GoogleExampleDesktop2.class})
public class DesktopTestsExample {
    @BeforeClass
    public static void beforeAllTests(){
        DriverProvider.startDrivers();
    }

    
    @AfterClass
    public static void afterAllTests(){
        DriverProvider.stopDrivers();
    }
}