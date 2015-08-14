package se.redmind.rmtest.selenium.grid;

import org.junit.runners.Parameterized;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RmAllDevice extends TestBase {

    private static List<Object> getDrivers() {
        return Arrays.asList(DriverProvider.getDrivers());
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        return getDrivers().stream()
                .map(obj -> new Object[]{obj, obj.toString()})
                .collect(Collectors.toList());
    }

    public RmAllDevice(DriverNamingWrapper driverWrapper, String initialUrl) {
        super(driverWrapper, "", initialUrl);
    }
}
