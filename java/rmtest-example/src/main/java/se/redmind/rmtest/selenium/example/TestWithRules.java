package se.redmind.rmtest.selenium.example;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.framework.RmTestWatcher;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

@RunWith(Parallelized.class)
public class TestWithRules {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WebDriver tDriver;
    private final DriverWrapper<?> driverWrapper;
    private final String driverDescription;
    private final RMReportScreenshot rmrScreenshot;

    public TestWithRules(DriverWrapper<?> driverWrapper, String driverDescription) {
        this.driverWrapper = driverWrapper;
        this.driverDescription = driverDescription;
        this.rmrScreenshot = new RMReportScreenshot(driverWrapper);
    }

    private static Object[] getDrivers() {
        return DriverProvider.getDrivers();

    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<>();
        Object[] wrapperList = getDrivers();
        for (Object wrapperList1 : wrapperList) {
            returnList.add(new Object[]{wrapperList1, wrapperList1.toString()});
        }

        return returnList;
    }

    @Rule
    public RmTestWatcher ruleExample = new RmTestWatcher();

    @Before
    public void beforeTest() {
        ruleExample.setDriver(this.driverWrapper);
    }

    @Test
    @Ignore
    public void testGoogle() throws Exception {
        HTMLPage navPage = new HTMLPage(this.tDriver);

        navPage.getDriver().get("http://www.comaround.se");
        // Find the text input element by its name

        logger.info("Page title is: " + navPage.getTitle());

        assertTrue(navPage.getTitle().startsWith("Z"));

        new RMReportScreenshot(driverWrapper).takeScreenshot(null);
        new RMReportScreenshot(driverWrapper).takeScreenshot("first");
        new RMReportScreenshot(driverWrapper).takeScreenshot("after");
        logger.info("Done!");
    }

    @Test
    public void testGoogle2() throws Exception {
        logger.info("StartOfTest");
        HTMLPage navPage = new HTMLPage(this.driverWrapper.getDriver());

        navPage.getDriver().get("http://www.comaround.se");
        assertTrue(false);
    }

}
