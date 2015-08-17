package se.redmind.rmtest.selenium.grid;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.runners.Parameterized;
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

    private static List<Object> getDrivers() {
        return Arrays.asList(DriverProvider.getDrivers());
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        return getDrivers().stream()
                .map(obj -> new Object[]{obj, obj.toString()})
                .collect(Collectors.toList());
    }
    
    public TestBase(final DriverNamingWrapper driverWrapper, final String deviceType, final String initialUrl) {
        this.driverNamingWrapper = driverWrapper;
        this.deviceType = deviceType;
        this.initialUrl = initialUrl;
    }

//    @Rule
//    public ScreenShotRule screenShot = new ScreenShotRule();
}
