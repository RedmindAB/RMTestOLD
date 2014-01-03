package se.aftonbladet.abtest.tests.mobile;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.aftonbladet.abtest.tests.mobile.menu.AbMobileFrontPageTest;
import se.aftonbladet.abtest.tests.mobile.menu.AbMobileNavTest;
import se.remind.rmtest.selenium.framework.TestParams;
import se.remind.rmtest.selenium.grid.DriverProvider;
               // AbMobileFrontPageTest.class, AbMobileNavTest.class,
@RunWith(Suite.class)
@Suite.SuiteClasses( {AbMobileNavTest.class, AbMobileFrontPageTest.class})
public class AbMobileTestRunner {

    @BeforeClass
    public static void setupTestSuiteParams() {
        TestParams.setBaseUrl("http://www.aftonbladet.se/");
        DriverProvider.startDrivers();
    }

    @AfterClass
    public static void tearDownTestSuite() {
        DriverProvider.stopDrivers();
    }
}
