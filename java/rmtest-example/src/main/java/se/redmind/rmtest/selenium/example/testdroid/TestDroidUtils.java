package se.redmind.rmtest.selenium.example.testdroid;

import com.testdroid.api.APIClient;
import com.testdroid.api.APIDeviceQueryBuilder;
import com.testdroid.api.APIException;
import com.testdroid.api.APIListResource;
import com.testdroid.api.APISort;
import com.testdroid.api.DefaultAPIClient;
import com.testdroid.api.model.APIDevice;

public class TestDroidUtils {

    public static APIClient CLIENT;

    public TestDroidUtils(String user, String passwd) {
        CLIENT = createApiClient("https://cloud.testdroid.com", user, passwd);
    }

    private void printDeviceNames(APIListResource<APIDevice> devicesResource) throws APIException {
        for (APIDevice device : devicesResource.getEntity().getData()) {
            System.out.println(device.getDisplayName());
        }
    }

    public APIDevice getFirstNonLockedDevice() throws APIException {
        APIListResource<APIDevice> devicesResource;
//        APIDeviceQueryBuilder query = new APIDeviceQueryBuilder().filterWithDeviceFilters(new )
//                .sort(APIDevice.class, new APISort.SortItem(APISort.Column.SOFTWARE_API_LEVEL, APISort.Type.ASC));
//        devicesResource = CLIENT.getDevices(query);
                devicesResource = CLIENT.getDevices(new APIDeviceQueryBuilder().offset(0).limit(10).search("")
                .sort(APIDevice.class, new APISort.SortItem(APISort.Column.SOFTWARE_API_LEVEL, APISort.Type.DESC)));
        for (APIDevice device : devicesResource.getEntity().getData()) {
            if (!device.isLocked()) {
                if (device.getOsType().equals(APIDevice.OsType.ANDROID)) {
                    if (device.getSoftwareVersion().getApiLevel() > 17) {
                        return device;
                    }
                }
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
//        System.out.println("FirstNonLockedDevice: " + getFirstNonLockedDevice(devicesResource).getDisplayName());
//        getFirstNonLockedDevice(devicesResource);
//
//        // Get new devices
//        devicesResource = CLIENT
//                .getDevices(new APIDeviceQueryBuilder().filterWithDeviceFilters(APIDevice.DeviceFilter.NEW));
//        System.out.println(String.format("\nGet %s new devices", devicesResource.getTotal()));
//        printDeviceNames(devicesResource);
//
//        // Get recommended devices
//        devicesResource = CLIENT
//                .getDevices(new APIDeviceQueryBuilder().filterWithDeviceFilters(APIDevice.DeviceFilter.RECOMMENDED));
//        System.out.println(String.format("\nGet %s recommended devices", devicesResource.getTotal()));
//        printDeviceNames(devicesResource);
//
//        // Get free devices
//        devicesResource = CLIENT
//                .getDevices(new APIDeviceQueryBuilder().filterWithDeviceFilters(APIDevice.DeviceFilter.FREE));
//        System.out.println(String.format("\nGet %s free devices", devicesResource.getTotal()));
//        printDeviceNames(devicesResource);
//
//        // Search device
//        String deviceName = devicesResource.getEntity().get(0).getDisplayName();
//        devicesResource = CLIENT.getDevices(new APIDeviceQueryBuilder().offset(0).limit(10).search(deviceName));
//        System.out.println(String.format("\nFound %s devices", devicesResource.getTotal()));
//        printDeviceNames(devicesResource);
//
//        // Search device with filter
//        devicesResource = CLIENT.getDevices(new APIDeviceQueryBuilder().offset(0).limit(10).search(deviceName)
//                .filterWithDeviceFilters(APIDevice.DeviceFilter.RECOMMENDED));
//        System.out.println(String.format("\nFound %s recommended devices", devicesResource.getTotal()));
//        printDeviceNames(devicesResource);
//
//        // Get devices using sorting
//        deviceNamreturn devicee = "Samsung Galaxy";
//        devicesResource = CLIENT.getDevices(new APIDeviceQueryBuilder().offset(0).limit(10).search(deviceName)
//                .sort(APIDevice.class, new APISort.SortItem(APISort.Column.DEVICE_NAME, APISort.Type.ASC)));
//        System.out.println(String.format("\nFound %s devices with name %s", devicesResource.getTotal(), deviceName));
//        printDeviceNames(devicesResource);
    }
}
