package se.redmind.rmtest.selenium.example;

import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.framework.RmTestWatcher;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

@RunWith(Parallelized.class)
public class TestWithRules {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WebDriver tDriver;
    private final DriverNamingWrapper urlContainer;
    private final String driverDescription;
    private final RMReportScreenshot rmrScreenshot;

    public TestWithRules(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        this.urlContainer = driverWrapper;
        this.driverDescription = driverDescription;
        this.rmrScreenshot = new RMReportScreenshot(urlContainer);
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
        ruleExample.setDriver(this.urlContainer);
    }

    @Test
    @Ignore
    public void testGoogle() throws Exception {
        HTMLPage navPage = new HTMLPage(this.tDriver);

        navPage.getDriver().get("http://www.comaround.se");
        // Find the text input element by its name

        logger.info("Page title is: " + navPage.getTitle());

        assertTrue(navPage.getTitle().startsWith("Z"));

        new RMReportScreenshot(urlContainer).takeScreenshot(null);
        new RMReportScreenshot(urlContainer).takeScreenshot("first");
        new RMReportScreenshot(urlContainer).takeScreenshot("after");
        logger.info("Done!");
    }

    @Test
    public void testGoogle2() throws Exception {
        logger.info("StartOfTest");
        HTMLPage navPage = new HTMLPage(this.urlContainer.getDriver());

        navPage.getDriver().get("http://www.comaround.se");
        assertTrue(false);
    }

}
