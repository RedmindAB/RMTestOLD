package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.selenium.WebDriver;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;



@RunWith(Parallelized.class)
public class CreateLogsTest {


	   private WebDriver tDriver;
	    private final DriverNamingWrapper driverWrapper;
	    private final String driverDescription;

	    public CreateLogsTest(final DriverNamingWrapper driverWrapper, final String driverDescription) {
	        this.driverWrapper = driverWrapper;
	        this.driverDescription = driverDescription;
	    }
	    
	    private static Object[] getDrivers() {
//	        return DriverProvider.getDrivers("rmDeviceType", "mobile");
//	    	return DriverProvider.getDrivers(Platform.ANDROID);
	    	return DriverProvider.getDrivers();

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

	//These test are just made to create a complete surefire log with all elements
    @Test
    public void nullPointerThrow() throws Exception {
    	throw new NullPointerException();
    }
    
    @Test
    public void isTrueSameAsFalse() throws Exception {
    	assertTrue(true == false);
    }
    
    @Test
    public void failThisTest() throws Exception {
    	fail("This failed");
    }
    
    @Test
    public void successTest() throws Exception {
    	assertTrue(true);
    }
}