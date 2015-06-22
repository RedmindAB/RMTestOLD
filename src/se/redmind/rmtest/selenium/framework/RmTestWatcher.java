package se.redmind.rmtest.selenium.framework;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;

public class RmTestWatcher extends TestWatcher{

    private DriverNamingWrapper driverContainer;

    public RmTestWatcher(){
        this.driverContainer = getDriverWrapper(driverContainer);
        System.out.println("Initializing RmTestWatcher");
    }

    public DriverNamingWrapper getDriverWrapper(DriverNamingWrapper driverWrapper){
        this.driverContainer = driverWrapper;
        return driverWrapper;
    }

    public void takeScreenShot(String className, String methodName){
        RMReportScreenshot RMRScreenshot = new RMReportScreenshot(driverContainer);
        RMRScreenshot.takeScreenshot(className, methodName, "Failed Testcase");
        System.out.println("----------> ScreenShot from: "+methodName+" taken! <----------");
    }

    @Override
    protected void failed(Throwable e, Description description){
        System.out.println("Test method failed! Description = "+description.getMethodName());
        takeScreenShot(description.getClassName(), description.getMethodName());
    }
}
