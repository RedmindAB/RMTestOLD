package se.redmind.rmtest.selenium.example;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Strings;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.rmtest.selenium.grid.RmAllDevice;
import se.redmind.utils.Try;

import se.redmind.rmtest.DriverWrapper;

import static org.junit.Assert.assertTrue;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;

@RunWith(Parallelized.class)
public class GoogleExample extends RmAllDevice {

    public GoogleExample(DriverWrapper<?> driverWrapper, final String driverDescription) {
        super(driverWrapper, driverDescription);
        driverWrapper.addCapabilities(
            (capabilities, description) -> capabilities.getBrowserName().equals("firefox"),
            capabilities -> {
                FirefoxProfile ffp = new FirefoxProfile();
                ffp.setPreference("webdriver.load.strategy", "unstable");
                capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");
                capabilities.setCapability(FirefoxDriver.PROFILE, ffp);
            });
        driverWrapper.addPostConfiguration(driver -> {
            logger.info("this will be executed only once");
        });
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

        logger.info("Done!");
    }

}
