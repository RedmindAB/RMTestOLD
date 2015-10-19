package se.redmind.rmtest.selenium.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.redmind.rmtest.comaround.test.ComaroundTest1;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.utils.LogBackUtil;

@RunWith(Suite.class)
@Suite.SuiteClasses({ComaroundTest1.class})
public class ComaroundTests {

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
