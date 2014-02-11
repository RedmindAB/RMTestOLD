package se.redmind.rmtest.selenium.tests.mobile;

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
public class RMDemo {
    
    private WebDriver tDriver;
    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;

    public RMDemo(final DriverNamingWrapper driverWrapper, final String driverDescription) {
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
  

    @Test 
    public void testAOS() throws Exception {
        tDriver = driverWrapper.getDriver();
       
        tDriver.get("http://www.aos.se");

        System.out.println("Page title is: " + tDriver.getTitle());
        assertTrue(tDriver.getTitle().startsWith("Allt om Stockholm"));
    }
    


}