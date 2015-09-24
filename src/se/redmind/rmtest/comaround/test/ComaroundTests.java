package se.redmind.rmtest.comaround.test;

import se.redmind.rmtest.selenium.grid.DriverProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ComaroundTest1.class})
public class ComaroundTests {
    @BeforeClass
    public static void beforeAllTests() {
    }

    @AfterClass
    public static void afterAllTests() {
        DriverProvider.stopDrivers();
    }
}
