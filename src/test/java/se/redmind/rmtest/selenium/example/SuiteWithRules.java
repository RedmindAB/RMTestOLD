package test.java.se.redmind.rmtest.selenium.example;

import se.redmind.rmtest.selenium.example.TestWithRules;
import se.redmind.rmtest.selenium.grid.DriverProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestWithRules.class})
public class SuiteWithRules {
    @BeforeClass
    public static void beforeAllTests() {
    }

    @AfterClass
    public static void afterAllTests() {
        DriverProvider.stopDrivers();
    }
}
