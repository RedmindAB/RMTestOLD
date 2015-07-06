package se.redmind.rmtest.selenium.framework;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;

public class RmTestWatcher extends TestWatcher{
	private DriverNamingWrapper driverContainer;
	
	public RmTestWatcher() {
//		this.driverContainer = driverContainer;
		System.out.println("Init of testhandler"); 
//		System.out.println("Driver: " + this.driverContainer.getDescription());
	}
	
    @Override
    protected void failed(Throwable e, Description description) {
        System.out.println("Onlyuted when a test fails. Failing Method: " + description.getClassName() + "." + description.getMethodName());
        String methodName = description.getMethodName();
        try {
        	int end = methodName.indexOf('[');
        	methodName = methodName.substring(0, end);
		} catch (Exception e2) {
		}
    	new RMReportScreenshot(driverContainer).takeScreenshot(description.getClassName(), methodName, "FailedTestcase");
    }
   
    
    @Override
    protected void finished(Description description) {
    	System.out.println("FinishRule");
    	this.driverContainer.getDriver().close();
    }
    
//    @Override
//    protected void starting(Description description) {
//    	System.out.println("StartingRule");
//    	this.driverContainer.startDriver();
//    	this.driverContainer.getDriver().get("www.google.se");
//    }
    
    public DriverNamingWrapper getDriverContainer() {
    	System.out.println("");
    	return driverContainer;
    }
    
    public void initDriver(DriverNamingWrapper pDriverContainer){
    	this.driverContainer = pDriverContainer;
    	this.driverContainer.startDriver();
    	
    }
}
