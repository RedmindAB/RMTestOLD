package se.redmind.rmtest.selenium.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.utils.LogBackUtil;

@RunWith(Suite.class)
@Suite.SuiteClasses({RMExample.class})
public class RedmindTests {

    static {
        LogBackUtil.install();
    }

    @BeforeClass
    public static void beforeAllTests() {

    }

    @AfterClass
    public static void afterAllTests() {
        DriverProvider.stopDrivers();
    }
}
