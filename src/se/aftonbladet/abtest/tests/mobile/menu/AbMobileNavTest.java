package se.aftonbladet.abtest.tests.mobile.menu;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.aftonbladet.abtest.navigation.mobil.AbMobileNav;
import se.redmind.rmtest.selenium.framework.TestParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

/**
 * @author oskeke
 */
@RunWith(Parallelized.class)
public class AbMobileNavTest {    // 
	private WebDriver tDriver;
	private AbMobileNav tNavPage;
    private String startUrl = TestParams.getBaseUrl();

    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;

    public AbMobileNavTest(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        this.driverWrapper = driverWrapper;
        this.driverDescription = driverDescription;
    }

    private static Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.ANDROID, Platform.MAC);

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
    //@Ignore
	public void ensure_mobile_nav_navigates_to_clicked_items() throws Exception {
		tDriver = driverWrapper.getDriver();
		System.out.println("Driver: " + tDriver);
		tNavPage = new AbMobileNav(tDriver, startUrl);
        tNavPage.driverFluentWait(45).until(ExpectedConditions.presenceOfElementLocated(By.id("abNavBarTopBtnLink")));

		tNavPage.clickLeftMenuItem("Nyheter");
		tNavPage.assertPageTitle("Nyheter");
		
		tNavPage.clickLeftMenuItem("Sport");
		tNavPage.assertPageTitle("Sport");
		
		tNavPage.clickLeftMenuItem("Nöje");
		tNavPage.assertPageTitle("Nöje");

        tNavPage.clickLeftMenuItem("Plus");
        tNavPage.assertPageTitle("Plus");
		
		tNavPage.clickLeftMenuItem("Ledare");
		tNavPage.assertPageTitle("Ledare");
		
		tNavPage.clickLeftMenuItem("Kultur");
		tNavPage.assertPageTitle("Kultur");
		
		tNavPage.clickLeftMenuItem("Debatt");
		tNavPage.assertPageTitle("Debatt");
		
		tNavPage.clickLeftMenuItem("Kolumnister");
		tNavPage.assertPageTitle("Kolumnister");

        tNavPage.clickLeftMenuItem("Om Aftonbladet");
        tNavPage.assertPageTitle("Här kan du läsa mer om Aftonbladet | Vanliga frågor | Hjälp & info | Aftonbladet");
	}
	
	

	@Test
    //@Ignore
	public void ensure_mobile_nav_navigates_to_clicked_submenu_items() throws Exception {
		tDriver = driverWrapper.getDriver();
		tNavPage = new AbMobileNav(tDriver, startUrl);
        tNavPage.driverFluentWait(45).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
		
		// need special impl of Sport and Nöje, submenu expanded if menu opened from Sport or Nöje.
		
		// Need special impl for Ledare, multilevel submenu.
		
		tNavPage.clickLeftSubMenuItem("Kultur", "Böcker");
		tNavPage.assertPageTitle("Bokrecensioner");
		
		tNavPage.clickLeftSubMenuItem("Kultur", "Konst");
		tNavPage.assertPageTitle("Konst");
		
		tNavPage.clickLeftSubMenuItem("Kultur", "Scen");
		tNavPage.assertPageTitle("Teater");
		
		tNavPage.clickLeftSubMenuItem("Kultur", "Serier");
		tNavPage.assertPageTitle("Serier");
		
		tNavPage.clickLeftSubMenuItem("Kultur", "Åsa Linderborg");
		tNavPage.assertPageTitle("Linderborg");
		
		// Kulturbloggar, multilevel. 
		
		tNavPage.clickLeftSubMenuItem("Kolumnister", "Robert Aschberg");
		tNavPage.assertPageTitle("Aschberg");
		
		tNavPage.clickLeftSubMenuItem("Kolumnister", "Lena Mellin");
		tNavPage.assertPageTitle("Mellin");
		
		//tNavPage.clickLeftMen("Visa mer", "Min ekonomi");
		//tNavPage.assertPageTitle("Min Ekonomi");

        tNavPage.clickLeftSubMenuItem("Visa mer", "Svenska hjältar");
        tNavPage.assertPageTitle("Svenska Hjältar");
		
		tNavPage.clickLeftSubMenuItem("Visa mer", "Wellness");
		tNavPage.assertPageTitle("Wellness");

        tNavPage.clickLeftSubMenuItem("Visa mer", "Wendela");
        tNavPage.assertPageTitle("Wendela");


        tNavPage.clickLeftSubMenuItem("Visa mer", "Resa");
		tNavPage.assertPageTitle("Resa");
		
		tNavPage.clickLeftSubMenuItem("Visa mer", "Bil");
		tNavPage.assertPageTitle("Bil");
	}
	
	@Test
    //@Ignore
	public void test_ensure_mobile_nav_navigates_to_clicked_topbar_items() throws Exception {
		tDriver = driverWrapper.getDriver();
		tNavPage = new AbMobileNav(tDriver, startUrl);
        tNavPage.driverFluentWait(45).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
		
		// Click Sport in top nav, then click all sub menu links:
		tNavPage.clickTopBarItem("Sport");
		tNavPage.assertPageTitle("Sport");
		
		tNavPage.clickTopBarSubItem("Fotboll");
		tNavPage.assertPageTitle("Fotboll");
		
		tNavPage.clickTopBarSubItem("Hockey");
		tNavPage.assertPageTitle("Hockey");
		
		// Add test to Resultat. Old framework, will require special test case.
		
		tNavPage.clickTopBarSubItem("Trav");
		tNavPage.assertPageTitle("Trav");
		
		
		// Click Nöje in top nav, then click all sub menu links:
		tNavPage.clickTopBarItem("Nöje");
		tNavPage.assertPageTitle("Nöje");

		tNavPage.clickTopBarSubItem("Schlager");
		tNavPage.assertPageTitle("Melodifestivalen | Nöjesbladet | Aftonbladet");
		
		tNavPage.clickTopBarSubItem("Spela");
		tNavPage.assertPageTitle("Spela");
		
		tNavPage.clickTopBarSubItem("Film");
		tNavPage.assertPageTitle("Film");
		
		tNavPage.clickTopBarSubItem("Klick!");
		tNavPage.assertPageTitle("Klick");
		
		//Click back to start page.
		tNavPage.clickTopBarItem("Start");
		tNavPage.assertPageTitle("Aftonbladet: Sveriges nyhetskälla och mötesplats");
	}

	@Test
    //@Ignore
	public void ensure_mobile_nav_navigates_to_external_page() throws Exception {
		tDriver = driverWrapper.getDriver();
		tNavPage = new AbMobileNav(tDriver, startUrl);
        tNavPage.driverFluentWait(45).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
		
		tNavPage.clickLeftMenuItemExternal("TV");
		tNavPage.assertPageTitle("Aftonbladet TV: Webbtv");
		
		tNavPage.navigateBack();
		tNavPage.assertPageTitle("Aftonbladet: Sveriges nyhetskälla och mötesplats");

        tNavPage.clickLeftMenuItemExternal("Bloggar");
        tNavPage.assertPageTitle("Aftonbladets bloggar");

		// ledarbloggar, SubSubLevel

		//Jan Helin
        tNavPage.navigateStartUrl();
		tNavPage.clickLeftSubMenuItemExternal("Bloggar", "Jan Helin");
		tNavPage.assertPageTitle("Jan Helin");
		tNavPage.navigateBack();
		tNavPage.assertPageTitle("Aftonbladet: Sveriges nyhetskälla och mötesplats");
		
		//Välkommen in!
        tNavPage.navigateStartUrl();
		tNavPage.clickLeftSubMenuItemExternal("Bloggar", "Välkommen in!");
		tNavPage.assertPageTitle("Välkommen in!");
		tNavPage.navigateBack();
		tNavPage.assertPageTitle("Aftonbladet: Sveriges nyhetskälla och mötesplats");


		
		//Nyhetsbloggar  -  SubSubLevel
		
		/*
		//SvD Näringsliv
		tNavPage.clickLeftMenuItemExternal("SvD Näringsliv");
		//tNavPage.assertPageTitle("SvD Näringsliv");
		assertTrue(tNavPage.pageTitleContains("SvD Näringsliv"));
		tNavPage.navigateBack();
		//tNavPage.assertPageTitle("Aftonbladet: Sveriges nyhetskälla och mötesplats");
		assertTrue(tNavPage.pageTitleContains("Aftonbladet: Sveriges nyhetskälla och mötesplats"));
		
		//SvD Näringsliv - Börsen
		tNavPage.clickLeftSubMenuItemExternal("SvD Näringsliv", "Börsen");
		//tNavPage.assertPageTitle("näringsliv börs");
		assertTrue(tNavPage.pageTitleContains("Näringsliv Börs"));
		tNavPage.navigateBack();
		//tNavPage.assertPageTitle("Aftonbladet: Sveriges nyhetskälla och mötesplats");
		assertTrue(tNavPage.pageTitleContains("Aftonbladet: Sveriges nyhetskälla och mötesplats"));
        */
	}

}
