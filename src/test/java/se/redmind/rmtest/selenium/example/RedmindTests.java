package test.java.se.redmind.rmtest.selenium.example;

import se.redmind.rmtest.selenium.example.RMExample;
import se.redmind.rmtest.selenium.grid.DriverProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({RMExample.class})
public class RedmindTests {
    @BeforeClass
    public static void beforeAllTests() {
    }

    @AfterClass
    public static void afterAllTests() {
        DriverProvider.stopDrivers();
    }
}