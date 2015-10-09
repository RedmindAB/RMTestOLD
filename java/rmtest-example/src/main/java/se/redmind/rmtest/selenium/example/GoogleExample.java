package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.rmtest.selenium.grid.RmAllDevice;

@RunWith(Parallelized.class)
public class GoogleExample extends RmAllDevice {

    public GoogleExample(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        super(driverWrapper, driverDescription);
        driverWrapper.addDriverConfig(new TestConfig());
    }

    @Before
    public void before() {
        this.webDriver = driverNamingWrapper.startDriver();
    }

    @Test
    public void testGoogle() throws Exception {
        HTMLPage navPage = new HTMLPage(driverNamingWrapper.getDriver());

        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name

        logger.info("Page title is: " + navPage.getTitle());

        assertTrue(navPage.getTitle().startsWith("Goo"));

        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot(null);
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("first");
        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("after");
        logger.info("Done!");

    }

    @Test
    public void testGoogle2() throws Exception {
        HTMLPage navPage = new HTMLPage(driverNamingWrapper.getDriver());

        navPage.getDriver().get("http://www.google.se");
        // Find the text input element by its name

        logger.info("Page title is: " + navPage.getTitle());

        assertTrue(navPage.getTitle().startsWith("Goo"));

        new RMReportScreenshot(this.driverNamingWrapper).takeScreenshot("");
        logger.info("Done!");

    }

}
