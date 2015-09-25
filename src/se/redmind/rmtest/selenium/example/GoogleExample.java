package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.assertTrue;

import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.rmtest.selenium.grid.RmAllDevice;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parallelized.class)
public class GoogleExample extends RmAllDevice {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleExample.class);

    public GoogleExample(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        super(driverWrapper, driverDescription);
        driverWrapper.addDriverConfig(new TestConfig());
    }

    @AfterClass
    public static void afterTest() {
    }

    @Before
    public void before() {
        this.webDriver = driverNamingWrapper.startDriver();
    }

    /*
     * @rmTest
     */
    @Test
    public void testGoogle() throws Exception {
        HTMLPage navPage = new HTMLPage(driverNamingWrapper.getDriver());
        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name
        LOG.debug("Page title is: " + navPage.getTitle());
        assertTrue(navPage.getTitle().startsWith("Goo"));
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot(null);
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("first");
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("after");
        LOG.debug("Done!");
    }

    @Test
    public void testGoogle2() throws Exception {
        HTMLPage navPage = new HTMLPage(driverNamingWrapper.getDriver());
        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name
        LOG.debug("Page title is: " + navPage.getTitle());
        assertTrue(navPage.getTitle().startsWith("Goo"));
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("");
        LOG.debug("Done!");
    }
}