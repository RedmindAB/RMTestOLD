package se.redmind.rmtest.selenium.grid;

import se.redmind.rmtest.DriverWrapper;

public abstract class RmAllDevice extends TestBase {

	public RmAllDevice(DriverWrapper<?> driverWrapper, final String driverDescription) {
		super(driverWrapper, driverDescription);
	}

}
