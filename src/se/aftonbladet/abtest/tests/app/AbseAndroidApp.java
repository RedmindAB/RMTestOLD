package se.aftonbladet.abtest.tests.app;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import se.aftonbladet.abtest.navigation.app.ABHybrid.AbseNav;
import se.redmind.rmtest.selenium.framework.TestParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

/**
 * Created with IntelliJ IDEA.
 * User: redben
 * Date: 10/04/14
 * Time: 08:59
 */

@RunWith(Parallelized.class)
public class AbseAndroidApp {
	private final DriverNamingWrapper driverWrapper;
	private final String driverDescription;
	private WebDriver tDriver;
	private AbseNav appNav;
	private String startUrl = TestParams.getBaseUrl();

	public AbseAndroidApp(final DriverNamingWrapper driverWrapper, final String driverDescription) {
		this.driverWrapper = driverWrapper;
		this.driverDescription = driverDescription;
	}

	private static Object[] getDrivers() {
		return DriverProvider.getDrivers(Platform.MAC);

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
	public void ABhybrid() throws Exception {
	WebDriver driver = driverWrapper.getDriver();

	appNav = new AbseNav(driver);
	appNav.OpenMenu();




//	appNav = new AbseNav (driver);
//	appNav.initialStartNoAction();
//        appNav.openMenu();
//	appNav.tryLeftMenu();
//        appNav.clickLeftMenuItem("Nyheter");
//        appNav.clickLeftMenuItem("Matcher");
//        appNav.clickLeftMenuItem("Resultat");
//        appNav.clickLeftMenuItem("Lag");
//        appNav.clickLeftMenuItem("TV");
//        appNav.clickLeftMenuItem("Bloggar");
//        appNav.clickLeftMenuItem("Silly Season");
}
}
