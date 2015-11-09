package se.redmind.rmtest.selenium.example.testdroid;

import com.testdroid.api.*;
import com.testdroid.api.model.APIDevice;
import com.testdroid.api.sample.util.Common;

import net.sourceforge.htmlunit.corejs.javascript.ast.CatchClause;

/**
 * @author Sławomir Pawluk <slawomir.pawluk@bitbar.com>
 */
public class TestDroidUtils {

    public static APIClient CLIENT;

    public TestDroidUtils(String user, String passwd) {
        CLIENT = createApiClient("https://cloud.testdroid.com", user, passwd);
    }

    private static void printDeviceNames(APIListResource<APIDevice> devicesResource) throws APIException {
        for (APIDevice device : devicesResource.getEntity().getData()) {
            System.out.println(device.getDisplayName());
        }
    }

    private static APIDevice getFirstNonLockedDevice(APIListResource<APIDevice> devicesResource) throws APIException {
        for (APIDevice device : devicesResource.getEntity().getData()) {
            if (!device.isLocked()) {
                return device;
            }
        }
        return null;

    }

    public static APIClient createApiClient(String url, String username, String password) {
        return new DefaultAPIClient(url, username, password);
    }
    
    public static void oldmethod(String[] args) throws APIException {
        APIListResource<APIDevice> devicesResource = CLIENT.getDevices();

        // System.out.println(String.format("Get %s devices",
        // devicesResource.getTotal()));
        // printDeviceNames(devicesResource);
        //
        // while (devicesResource.isNextAvailable()) {
        // devicesResource = devicesResource.getNext();
        // printDeviceNames(devicesResource);
        // }

        // Get A device
        System.out.println("FirstNonLockedDevice: " + getFirstNonLockedDevice(devicesResource).getDisplayName());
        getFirstNonLockedDevice(devicesResource);

        // Get new devices
        devicesResource = CLIENT
                .getDevices(new APIDeviceQueryBuilder().filterWithDeviceFilters(APIDevice.DeviceFilter.NEW));
        System.out.println(String.format("\nGet %s new devices", devicesResource.getTotal()));
        printDeviceNames(devicesResource);

        // Get recommended devices
        devicesResource = CLIENT
                .getDevices(new APIDeviceQueryBuilder().filterWithDeviceFilters(APIDevice.DeviceFilter.RECOMMENDED));
        System.out.println(String.format("\nGet %s recommended devices", devicesResource.getTotal()));
        printDeviceNames(devicesResource);

        // Get free devices
        devicesResource = CLIENT
                .getDevices(new APIDeviceQueryBuilder().filterWithDeviceFilters(APIDevice.DeviceFilter.FREE));
        System.out.println(String.format("\nGet %s free devices", devicesResource.getTotal()));
        printDeviceNames(devicesResource);

        // Search device
        String deviceName = devicesResource.getEntity().get(0).getDisplayName();
        devicesResource = CLIENT.getDevices(new APIDeviceQueryBuilder().offset(0).limit(10).search(deviceName));
        System.out.println(String.format("\nFound %s devices", devicesResource.getTotal()));
        printDeviceNames(devicesResource);

        // Search device with filter
        devicesResource = CLIENT.getDevices(new APIDeviceQueryBuilder().offset(0).limit(10).search(deviceName)
                .filterWithDeviceFilters(APIDevice.DeviceFilter.RECOMMENDED));
        System.out.println(String.format("\nFound %s recommended devices", devicesResource.getTotal()));
        printDeviceNames(devicesResource);

        // Get devices using sorting
        deviceName = "Samsung Galaxy";
        devicesResource = CLIENT.getDevices(new APIDeviceQueryBuilder().offset(0).limit(10).search(deviceName)
                .sort(APIDevice.class, new APISort.SortItem(APISort.Column.DEVICE_NAME, APISort.Type.ASC)));
        System.out.println(String.format("\nFound %s devices with name %s", devicesResource.getTotal(), deviceName));
        printDeviceNames(devicesResource);

    }
}
