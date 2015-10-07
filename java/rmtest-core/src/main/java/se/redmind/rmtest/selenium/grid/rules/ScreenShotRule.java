package se.redmind.rmtest.selenium.grid.rules;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;

/**
 * Created by johgri on 15-06-22.
 */
public class ScreenShotRule extends TestWatcher {

	private static final Logger LOG = LoggerFactory.getLogger(ScreenShotRule.class);

	private DriverNamingWrapper driverContainer;

	public ScreenShotRule() {
		this.driverContainer = setDriverWrapper(driverContainer);
		LOG.debug("Initializing ScreenShotRule");
	}

	public DriverNamingWrapper setDriverWrapper(DriverNamingWrapper driverWrapper) {
		this.driverContainer = driverWrapper;
		return driverWrapper;
	}

	public void takeScreenShot(String className, String methodName) {
		try {
			RMReportScreenshot RMRScreenshot = new RMReportScreenshot(driverContainer);
			RMRScreenshot.takeScreenshot(className, methodName, "Failed Testcase");
			LOG.debug("----------> ScreenShot from: " + methodName + " taken! <----------");
		} catch (Exception e) {
			LOG.error("Error taking screenshot from method: "+methodName);
		}
	}

	@Override
	protected void failed(Throwable e, Description description) {
		LOG.debug("Test method failed! Description = " + description.getMethodName());
		String methodName = description.getMethodName();
		try {
			int end = methodName.indexOf('[');
			methodName = methodName.substring(0, end);
		} catch (Exception e2) {
		}
		takeScreenShot(description.getClassName(), methodName);

	}
}
