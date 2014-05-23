package test.java;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import se.aftonbladet.abtest.tests.app.AbseAndroidApp;
import se.redmind.rmtest.selenium.grid.DriverProvider;

@RunWith(Suite.class)
@Suite.SuiteClasses({AbseAndroidApp.class} )
public class ABseRunner {
	@BeforeClass
	public static void beforeABseRunner(){
		DriverProvider.startDrivers();

	}

	@AfterClass
	public static void afterABseRunner(){
		//DriverProvider.stopDrivers();

	}
}