package se.aftonbladet.abtest.tests.mobile;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.aftonbladet.abtest.tests.mobile.menu.AbMobileNavTest;

/**
 * Regression tests for the mobile web site.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {AbMobileNavTest.class })
public class MobileRegressionTestSuiteWithFirefox {

	@BeforeClass
	public static void beforeAll() {
		System.setProperty("se.aftonbladet.driver", "android");
	}

}
