package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.assertTrue;

import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.framework.RmTestWatcher;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parallelized.class)
public class CopyOfGoogleExample {

    private static final Logger LOG = LoggerFactory.getLogger(CopyOfGoogleExample.class);

    public @Rule RmTestWatcher testWatcher = new RmTestWatcher();

    private WebDriver tDriver;
    private final DriverNamingWrapper urlContainer;
    private final String driverDescription;
    private final RMReportScreenshot rmrScreenshot;

    public CopyOfGoogleExample(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        this.urlContainer = driverWrapper;
        this.driverDescription = driverDescription;
        this.rmrScreenshot = new RMReportScreenshot(urlContainer);
    }

    private static Object[] getDrivers() {
        return DriverProvider.getDrivers();
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
        Object[] wrapperList = getDrivers();
        for(int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
        }
        return returnList;
    }

    @AfterClass
    public static void afterTest() {
    }

    @Before
    public void beforeTest() {
        this.tDriver = urlContainer.startDriver();
        testWatcher.setDriver(urlContainer);
    }

    @Test
    public void testGoogle() throws Exception {
        HTMLPage navPage = new HTMLPage(this.tDriver);
        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name
        LOG.debug("Page title is: " + navPage.getTitle());
        assertTrue(navPage.getTitle().startsWith("Goo"));
        new RMReportScreenshot(urlContainer).takeScreenshot(null);
        new RMReportScreenshot(urlContainer).takeScreenshot("first");
        new RMReportScreenshot(urlContainer).takeScreenshot("after");
        LOG.debug("Done!");
        assertTrue(true);
    }

    @Test
    public void testGoogle2() throws Exception {
        HTMLPage navPage = new HTMLPage(this.tDriver);
        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name
        LOG.debug("Page title is: " + navPage.getTitle());
        assertTrue(navPage.getTitle().startsWith("Goo"));
        new RMReportScreenshot(urlContainer).takeScreenshot("");
        LOG.debug("Done!");
    }
}