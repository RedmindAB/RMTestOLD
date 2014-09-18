package se.redmind.rmtest.selenium.framework;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

@RunWith(Parallelized.class)
public class ParameterizedTest {
    
    protected WebDriver tDriver;
    protected final DriverNamingWrapper driverWrapper;
    protected final String driverDescription;

    public ParameterizedTest(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        this.driverWrapper = driverWrapper;
        this.driverDescription = driverDescription;
    }
    
    private static Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.ANDROID);
//      return DriverProvider.getDrivers("platform", "VISTA");
//    	return DriverProvider.getDrivers("deviceId", "SH35GW901373");

    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
        Object[] wrapperList = getDrivers();
        for (int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
        }

        return returnList;
    }

}