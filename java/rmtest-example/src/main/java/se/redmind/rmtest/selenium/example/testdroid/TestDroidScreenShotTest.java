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
import se.redmind.rmtest.config.TestDroidConfiguration;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

@RunWith(Parallelized.class)
public class TestDroidScreenShotTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDroidScreenShotTest.class);

    private final TestDroidDriverWrapper wrapper;

    public TestDroidScreenShotTest(TestDroidDriverWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @BeforeClass
    public static void setup() throws IOException, APIException {
        TestDroidDriverWrapper wrapper = (TestDroidDriverWrapper) DriverProvider.getDrivers()[0];
        Optional<APIDevice> potentialDevice = wrapper.getFirstNonLockedDevice(APIDevice.OsType.ANDROID, 21);
        if (potentialDevice.isPresent()) {
            APIDevice device = potentialDevice.get();
            LOGGER.info("found testdroid device: " + device.getDisplayName());
            TestDroidConfiguration configuration = wrapper.getConfiguration();
            String fileUUID = TestDroidDriverWrapper.uploadFile(configuration.appPath, configuration.serverUrl + "/upload", configuration.username, configuration.password);
            
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
            .map(driver -> new Object[]{driver})
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
