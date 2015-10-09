package se.redmind.rmtest.selenium.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import se.redmind.rmtest.selenium.grid.DriverProvider;

@RunWith(Suite.class)
@Suite.SuiteClasses({GoogleExample.class})
public class GoogleTests {

    @BeforeClass
    public static void beforeAllTests() {

    }

    @AfterClass
    public static void afterAllTests() {
        DriverProvider.stopDrivers();
    }
}
