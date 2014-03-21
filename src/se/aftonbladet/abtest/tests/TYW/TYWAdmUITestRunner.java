package se.aftonbladet.abtest.tests.TYW;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import se.aftonbladet.abtest.navigation.TYW.TYWAdmUIParams;
import se.redmind.rmtest.selenium.grid.DriverProvider;


/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-20
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestTYWAdmUI.class})
public class TYWAdmUITestRunner {

    @BeforeClass
    public static void setupTestSuiteParams() {
        TYWAdmUIParams.setBaseUrl("http://www.stage3.abse.aftonbladet.se:8088/");
        DriverProvider.startDrivers();
    }

    @AfterClass
    public static void tearDownTestSuite() {
        DriverProvider.stopDrivers();
    }
}
