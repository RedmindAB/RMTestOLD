package se.redmind.rmtest.selenium.grid;

import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;

public abstract class TestBase {
    public static final String TABLET_DEVICE = "tablet";
    public static final String MOBILE_DEVICE = "mobile";
    protected final DriverNamingWrapper driverNamingWrapper;
    private final String deviceType;
    private final String initialUrl;
    protected WebDriver webDriver;
    private static final Logger LOG = LoggerFactory.getLogger(ScreenShotRule.class);

    public TestBase(final DriverNamingWrapper driverWrapper, final String deviceType, final String initialUrl) {
        this.driverNamingWrapper = driverWrapper;
        this.deviceType = deviceType;
        this.initialUrl = initialUrl;
        this.webDriver = driverWrapper.startDriver();
    }

    @Rule
    public ScreenShotRule screenShot = new ScreenShotRule();
}
