package se.redmind.rmtest.selenium.example.testdroid;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testdroid.api.APIException;
import com.testdroid.api.model.APIDevice;
import se.redmind.rmtest.TestDroidDriverWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

@RunWith(Parallelized.class)
public class TestDroidScreenShotTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDroidScreenShotTest.class);
    private static final String TARGET_APP_PATH = "/Users/jeremy/ClonyBird/output/clonybird.apk";
    private static final String TESTDROID_SERVER = "https://appium.testdroid.com";
    private static final String TESTDROID_USERNAME = "petter.osterling@redmind.se";
    private static final String TESTDROID_PASSWORD = "Redmind1";

    private final TestDroidDriverWrapper wrapper;
    private final String driverDescription;

    public TestDroidScreenShotTest(TestDroidDriverWrapper wrapper, String driverDescription) {
        this.wrapper = wrapper;
        this.driverDescription = driverDescription;
    }

    @BeforeClass
    public static void setup() throws IOException, APIException {
        TestDroidDriverWrapper wrapper = (TestDroidDriverWrapper) DriverProvider.getDrivers()[0];
        Optional<APIDevice> potentialDevice = wrapper.getFirstNonLockedDevice(APIDevice.OsType.ANDROID, 21);
        if (potentialDevice.isPresent()) {
            APIDevice device = potentialDevice.get();
            LOGGER.info("found testdroid device: " + device.getDisplayName());
            String fileUUID = wrapper.uploadFile(TARGET_APP_PATH, TESTDROID_SERVER, TESTDROID_USERNAME, TESTDROID_PASSWORD);

            wrapper.getCapability().setCapability("platformName", "Android");
            wrapper.getCapability().setCapability("testdroid_target", "Android");
            wrapper.getCapability().setCapability("deviceName", "Android Device");
            wrapper.getCapability().setCapability("testdroid_project", "LocalAppium");
            wrapper.getCapability().setCapability("testdroid_testrun", "Android Run 1");
            wrapper.getCapability().setCapability("testdroid_device", device.getDisplayName()); // Freemium device
            wrapper.getCapability().setCapability("testdroid_app", fileUUID); //to use existing app using "latest" as fileUUID
            LOGGER.info(wrapper.getCapability().toString());
            LOGGER.info("Creating Appium session, this may take couple minutes..");
            wrapper.getDriver();
        } else {
            LOGGER.error("didn't find any device matching our criterias ...");
        }
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        return Arrays.asList(DriverProvider.getDrivers()).stream()
            .filter(driver -> driver instanceof TestDroidDriverWrapper)
            .map(driver -> new Object[]{driver, driver.toString()})
            .collect(Collectors.toList());
    }

    @AfterClass
    public static void tearDown() {
        DriverProvider.stopDrivers();
    }

    @Test
    public void mainPageTest() throws IOException, InterruptedException {
        wrapper.takeScreenshot("start");
    }
}
