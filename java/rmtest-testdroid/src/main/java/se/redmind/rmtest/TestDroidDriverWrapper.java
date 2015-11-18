package se.redmind.rmtest;

import java.util.Optional;
import java.util.function.Function;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.testdroid.api.*;
import com.testdroid.api.model.APIDevice;
import io.appium.java_client.AppiumDriver;

/**
 * @author Jeremy Comte
 */
public class TestDroidDriverWrapper extends AppiumDriverWrapper {

    private final APIClient client;
    private final int maxDevices;

    public TestDroidDriverWrapper(APIClient client, int maxDevices, DesiredCapabilities capabilities, String description, Function<DesiredCapabilities, AppiumDriver<WebElement>> function) {
        super(capabilities, description, function);
        this.client = client;
        this.maxDevices = maxDevices;
    }

    public Optional<APIDevice> getFirstNonLockedDevice(APIDevice.OsType osType, int requiredApiLevel) throws APIException {
        APIListResource<APIDevice> devicesResource = client.getDevices(new APIDeviceQueryBuilder().offset(0).limit(maxDevices).search("")
            .sort(APIDevice.class, new APISort.SortItem(APISort.Column.SOFTWARE_API_LEVEL, APISort.Type.DESC)));
        for (APIDevice device : devicesResource.getEntity().getData()) {
            if (!device.isLocked()) {
                if (device.getOsType().equals(osType)) {
                    if (device.getSoftwareVersion().getApiLevel() >= requiredApiLevel) {
                        return Optional.of(device);
                    }
                }
            }
        }

        return Optional.empty();
    }

}
