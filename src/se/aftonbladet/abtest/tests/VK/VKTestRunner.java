package se.aftonbladet.abtest.tests.VK;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import se.aftonbladet.abtest.navigation.VK.VKParams;
import se.redmind.rmtest.selenium.grid.DriverProvider;

/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-22
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses( {VKDesktopTestCase.class})
public class VKTestRunner {
    @BeforeClass
    public static void setupTestSuiteParams() {
        VKParams.setBaseUrl("http://stage.viktklubb.aftonbladet.se/v4");
        DriverProvider.startDrivers();
    }

    @AfterClass
    public static void tearDownTestSuite() {
        DriverProvider.stopDrivers();
    }
}
