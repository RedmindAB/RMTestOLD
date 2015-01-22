package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;



import se.redmind.rmtest.selenium.framework.StackTraceInfo;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;



@RunWith(Parallelized.class)
public class CreateLogsTestSecond {


	   private WebDriver tDriver;
	    private final DriverNamingWrapper driverWrapper;
	    private final String driverDescription;

	    public CreateLogsTestSecond(final DriverNamingWrapper driverWrapper, final String driverDescription) {
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


    @Test
    public void asertion1Equals2() throws Exception {
    	assertEquals(1, 2);
    }
    
    @Test
    public void exceptionTrown() throws Exception {
    	throw new IllegalAccessError();
    }


}