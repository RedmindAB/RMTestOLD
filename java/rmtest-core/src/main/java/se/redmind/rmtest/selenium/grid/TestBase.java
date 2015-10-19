package se.redmind.rmtest.selenium.grid;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TestBase {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final DriverNamingWrapper driverNamingWrapper;
    protected WebDriver webDriver;

    public TestBase(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        this.driverNamingWrapper = driverWrapper;
    }

    private static List<Object> getDrivers() {
        return Arrays.asList(DriverProvider.getDrivers());
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        return getDrivers().stream().map(obj -> new Object[]{obj, obj.toString()}).collect(Collectors.toList());
    }

}
