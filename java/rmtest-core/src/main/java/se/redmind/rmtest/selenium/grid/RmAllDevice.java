package se.redmind.rmtest.selenium.grid;

import org.junit.runners.Parameterized;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RmAllDevice extends TestBase {

	public RmAllDevice(final DriverNamingWrapper driverWrapper, final String driverDescription) {
		super(driverWrapper, driverDescription);
	}

}
