package se.redmind.rmtest.selenium.example;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Strings;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.rmtest.selenium.grid.RmAllDevice;
import se.redmind.utils.Try;

import se.redmind.rmtest.DriverWrapper;

import static org.junit.Assert.assertTrue;

@RunWith(Parallelized.class)
public class GoogleExample extends RmAllDevice {

    public GoogleExample(DriverWrapper<?> driverWrapper, final String driverDescription) {
        super(driverWrapper, driverDescription);
        driverWrapper.addDriverConfig(new TestConfig());
    }

    @Test
    public void testGoogle() throws Exception {
        HTMLPage navPage = new HTMLPage(driverWrapper.getDriver());

        navPage.getDriver().get("http://www.google.se");

        String pageTitle = Try.toGet(() -> navPage.getTitle())
            .until(value -> !Strings.isNullOrEmpty(value))
            .delayRetriesBy(500)
            .nTimes(10);

        logger.info("Page title is: " + pageTitle);

        assertTrue(pageTitle.startsWith("Goo"));

        new RMReportScreenshot(this.driverWrapper).takeScreenshot("first");
        new RMReportScreenshot(this.driverWrapper).takeScreenshot("after");
        logger.info("Done!");
    }

}
