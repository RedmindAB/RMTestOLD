package se.redmind.rmtest.selenium.framework;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmTestWatcher extends TestWatcher {
    private static final Logger LOG = LoggerFactory.getLogger(RmTestWatcher.class);
    private DriverNamingWrapper driverContainer;

    public RmTestWatcher() {
        LOG.debug("Init of testhandler");
    }

    @Override
    protected void failed(Throwable e, Description description) {
        LOG.debug("Onlyuted when a test fails. Failing Method: " + description.getClassName() + "." + description
                .getMethodName());
        String methodName = description.getMethodName();
        try {
            int end = methodName.indexOf('[');
            methodName = methodName.substring(0, end);
        }
        catch(Exception e2) {
            LOG.debug("", e);
        }
        new RMReportScreenshot(driverContainer).takeScreenshot(description.getClassName(),
                methodName,
                "FailedTestcase");
    }

    public DriverNamingWrapper getDriverWrapper(DriverNamingWrapper driverWrapper) {
        this.driverContainer = driverWrapper;
        return driverWrapper;
    }

    public void setDriver(DriverNamingWrapper pDriverContainer) {
        this.driverContainer = pDriverContainer;
    }
}
