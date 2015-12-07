package se.redmind.rmtest.selenium.example;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.runners.DriverRunner;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.framework.RmTestWatcher;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

@RunWith(DriverRunner.class)
public class TestWithRules {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WebDriver tDriver;
    private final DriverWrapper<?> driverWrapper;
    private final RMReportScreenshot rmrScreenshot;

    public TestWithRules(DriverWrapper<?> driverWrapper) {
        this.driverWrapper = driverWrapper;
        this.rmrScreenshot = new RMReportScreenshot(driverWrapper);
    }

    @Rule
    public RmTestWatcher ruleExample = new RmTestWatcher();

    @Before
    public void beforeTest() {
        ruleExample.setDriver(driverWrapper);
    }

    @Test
    public void test() throws Exception {
        logger.info("StartOfTest");
        HTMLPage navPage = new HTMLPage(this.driverWrapper.getDriver());

        navPage.getDriver().get("http://www.comaround.se");
        assertTrue(false);
    }

}