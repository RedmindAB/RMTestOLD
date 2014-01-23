package se.aftonbladet.abtest.tests.TYW;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import se.aftonbladet.abtest.navigation.TYW.TYWParams;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.aftonbladet.abtest.tests.TYW.TestTYWBackend;


/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-20
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestTYWBackend.class})
public class TYWTestRunner {

    @BeforeClass
    public static void setupTestSuiteParams() {
        TYWParams.setBaseUrl("http://stage.feature.aftonbladet.se:9000/");
        DriverProvider.startDrivers();
    }

    @AfterClass
    public static void tearDownTestSuite() {
        DriverProvider.stopDrivers();
    }
}
