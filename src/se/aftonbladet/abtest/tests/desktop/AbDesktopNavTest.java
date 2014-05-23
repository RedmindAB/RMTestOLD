package se.aftonbladet.abtest.tests.desktop;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import se.aftonbladet.abtest.navigation.mobil.AbBasicNav;
import se.redmind.rmtest.selenium.framework.TestParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

/**
 * Created with IntelliJ IDEA.
 * User: Reda Benmeradi
 * Date: 2013-09-16
 * Time: 13:48
 */

@RunWith(Parallelized.class)
public class AbDesktopNavTest {    //
	private WebDriver tDriver;
	private AbBasicNav tNavPage;
	private String startUrl = TestParams.getBaseUrl();

	private final DriverNamingWrapper driverWrapper;
	private final String driverDescription;

	public AbDesktopNavTest(final DriverNamingWrapper driverWrapper, final String driverDescription) {
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


/**@RunWith(JUnitParamsRunner.class)
public class AbDesktopNavTest {
private WebDriver tDriver;
private AbBasicNav tNavPage;

private Object[] getDrivers() {
return DriverProvider.getDrivers(Platform.MAC, "safari");
}
 */

		@Test
		@Ignore
		public void Desktop_Login_credentials() throws Exception {
			WebDriver tDriver = driverWrapper.getDriver();
			System.out.println("Driver: " + tDriver);
			tNavPage = new AbBasicNav(tDriver);

			tNavPage.Login("fb.abse@gmail.com", "aftonbladet.se");

		}

		@Test
		@Ignore

		public void Sportbladet_Menu_and_Sub_menu ()throws Exception {
			WebDriver tDriver = driverWrapper.getDriver();
			System.out.println("Driver: " + tDriver);
			tNavPage = new AbBasicNav(tDriver);

			tNavPage.clickOnMenuItem("Sport");
			tNavPage.pageTitleContains("Sport");


			tNavPage.clickOnSubMenuItem("LIVE/Mål för mål");
			tNavPage.pageTitleContains("Mål för mål");

				tNavPage.clickOnSubMenuItemLv2("Fotboll Live");
				tNavPage.pageTitleContains("Europa Live");

				tNavPage.clickOnSubMenuItemLv2("Allsvenskan Live");
				tNavPage.pageTitleContains("Allsvenskan Live");

				tNavPage.clickOnSubMenuItemLv2("Leifby Live");
				tNavPage.pageTitleContains("Leifby Live");

				tNavPage.clickOnSubMenuItemLv2("SHL Live");
				tNavPage.pageTitleContains("SHL Live");

				tNavPage.clickOnSubMenuItemLv2("HockeyAllsvenskan Live");
				tNavPage.pageTitleContains("HockeyAllsvenskan Live");

				tNavPage.clickOnSubMenuItemLv2("F1-bevakaren");
				tNavPage.pageTitleContains("F1-bevakaren");

			tNavPage.clickOnSubMenuItem("Resultat/Tabeller");
			tNavPage.pageTitleContains("Resultat/Tabeller");

			tNavPage.clickOnSubMenuItem("OS 2014");
			tNavPage.pageTitleContains("OS 2014");

			tNavPage.clickOnSubMenuItem("Fotboll");
			tNavPage.pageTitleContains("Fotboll");

				tNavPage.clickOnSubMenuItemLv2("Landslaget");
				tNavPage.pageTitleContains("Landslaget");

				tNavPage.clickOnSubMenuItemLv2("Damlandslaget");
				tNavPage.pageTitleContains("Damlandslaget");

				tNavPage.clickOnSubMenuItemLv2("VM-kvalet");
				tNavPage.pageTitleContains("VM-kvalet");

				tNavPage.clickOnSubMenuItemLv2("Allsvenskan");
				tNavPage.pageTitleContains("Allsvenskan");

				tNavPage.clickOnSubMenuItemLv2("Damallsvenskan");
				tNavPage.pageTitleContains("Damallsvenskan");

				tNavPage.clickOnSubMenuItemLv2("Premier League");
				tNavPage.pageTitleContains("Premier League");

				tNavPage.clickOnSubMenuItemLv2("La Liga");
				tNavPage.pageTitleContains("La liga");

				tNavPage.clickOnSubMenuItemLv2("Serie A");
				tNavPage.pageTitleContains("Serie A");

				tNavPage.clickOnSubMenuItemLv2("Bundesliga");
				tNavPage.pageTitleContains("Bundesliga");

				tNavPage.clickOnSubMenuItemLv2("Ligue 1");
				tNavPage.pageTitleContains("Ligue 1");

				tNavPage.clickOnSubMenuItemLv2("Zlatan");
				tNavPage.pageTitleContains("Zlatan");

				tNavPage.clickOnSubMenuItemLv2("Guldbollen");
				tNavPage.pageTitleContains("Guldbollen");

			tNavPage.clickOnSubMenuItem("Hockey");
			tNavPage.pageTitleContains("Hockey");

				tNavPage.clickOnSubMenuItemLv2("Hockey-VM");
				tNavPage.pageTitleContains("Hocket-VM");

				tNavPage.clickOnSubMenuItemLv2("Tre Kronor");
				tNavPage.pageTitleContains("Tre Kronor");

				tNavPage.clickOnSubMenuItemLv2("Damkronorna");
				tNavPage.pageTitleContains("Damkronorna");

				tNavPage.clickOnSubMenuItemLv2("SHL");
				tNavPage.pageTitleContains("SHL");

				tNavPage.clickOnSubMenuItemLv2("Hockeyallsvenskan");
				tNavPage.pageTitleContains("Hocketallsvenskan");

				tNavPage.clickOnSubMenuItemLv2("Riksserien");
				tNavPage.pageTitleContains("Riksserien");

				tNavPage.clickOnSubMenuItemLv2("NHL");
				tNavPage.pageTitleContains("NHL");

				tNavPage.clickOnSubMenuItemLv2("KHL");
				tNavPage.pageTitleContains("KHL");

			tNavPage.clickOnSubMenuItem("Motor");
			tNavPage.pageTitleContains("Motor");

				tNavPage.clickOnSubMenuItemLv2("Formel 1");
				tNavPage.pageTitleContains("Formel 1");

				tNavPage.clickOnSubMenuItemLv2("Speedway");
				tNavPage.pageTitleContains("speedway");

			tNavPage.clickOnSubMenuItem("Trav365");
			tNavPage.pageTitleContains("Trav365");

			tNavPage.clickOnSubMenuItemLv2("Startlistor");
			tNavPage.pageTitleContains("Startlistor");

			tNavPage.clickOnSubMenuItemLv2("V86-tips");
			tNavPage.pageTitleContains("V86-tips");

			tNavPage.clickOnSubMenuItemLv2("V75-tips");
			tNavPage.pageTitleContains("V75-tips");

			tNavPage.clickOnSubMenuItemLv2("V65-tips");
			tNavPage.pageTitleContains("V65-tips");

			tNavPage.clickOnSubMenuItemLv2("V64-tips");
			tNavPage.pageTitleContains("v64-tips");

			tNavPage.clickOnSubMenuItemLv2("V4-tips");
			tNavPage.pageTitleContains("V4-tips");

			tNavPage.clickOnSubMenuItemLv2("DD-tips");
			tNavPage.pageTitleContains("DD-tips");

			tNavPage.clickOnSubMenuItemLv2("Åke Svanstedt");
			tNavPage.pageTitleContains("Åke Svanstedt");

			tNavPage.clickOnSubMenuItemLv2("Björn Goop");
			tNavPage.pageTitleContains("Björn Goop");

			tNavPage.clickOnSubMenuItemLv2("Stig H:s V86-tips");
			tNavPage.pageTitleContains("Stig H:s V86-tips");

			tNavPage.clickOnSubMenuItemLv2("Stig H:s V75-tips");
			tNavPage.pageTitleContains("Stig H:s V75-tips");

			tNavPage.clickOnSubMenuItemLv2("Miljonklubben");
			tNavPage.pageTitleContains("Miljonklubben");

			tNavPage.clickOnSubMenuItemLv2("Intervjuer till V86");
			tNavPage.pageTitleContains("Intervjuer till V86");

			tNavPage.clickOnSubMenuItemLv2("Intervjuer till V75");
			tNavPage.pageTitleContains("Intervjuer till V75");

			tNavPage.clickOnSubMenuItemLv2("Lutfi Kolgjini");
			tNavPage.pageTitleContains("Lutfi Kolgjini");

			tNavPage.clickOnSubMenuItemLv2("Challes tips");
			tNavPage.clickOnSubMenuItemLv2("Micke Nybrink");
			tNavPage.clickOnSubMenuItemLv2("V75 Tillsammans");

			tNavPage.clickOnSubMenuItem("Spel");
			tNavPage.pageTitleContains("Spel");

				tNavPage.clickOnSubMenuItemLv2("Dagens Bästa Spel");
				tNavPage.pageTitleContains("Dagens Bästa Spel");

				tNavPage.clickOnSubMenuItemLv2("Bomben & Lången");
				tNavPage.pageTitleContains("Bomben & Lången");

				tNavPage.clickOnSubMenuItemLv2("Stryktipset");
				tNavPage.pageTitleContains("Stryktipset");

				tNavPage.clickOnSubMenuItemLv2("Power Play");
				tNavPage.pageTitleContains("Power Play");

				tNavPage.clickOnSubMenuItemLv2("Långenlistan");
				tNavPage.pageTitleContains("Långenlistan");


			tNavPage.clickOnSubMenuItem("S24");
			tNavPage.pageTitleContains("S24");

				tNavPage.clickOnSubMenuItemLv2("S24 Arkiv");
				tNavPage.pageTitleContains("S24 Arkiv");


				tNavPage.clickOnSubMenuItemLv2("Så här gör du");
				tNavPage.pageTitleContains("Så här gör du");

				tNavPage.clickOnSubMenuItemLv2("Frågor och svar");
				tNavPage.pageTitleContains("Frågor och svar");

				tNavPage.clickOnSubMenuItemLv2("Kontakta oss");
				tNavPage.pageTitleContains("Kontakta oss");

			tNavPage.clickOnSubMenuItem("Biblar");
			tNavPage.pageTitleContains("Biblar");

			tNavPage.clickOnSubMenuItem("Mer sport");
			//tNavPage.assertPageTitle("Mer sport");
		}

		@Test
		@Ignore

		public void Noje_Menu_and_Sub_menu ()throws Exception {
			WebDriver tDriver = driverWrapper.getDriver();
			System.out.println("Driver: " + tDriver);
			tNavPage = new AbBasicNav(tDriver);


			tNavPage.clickOnMenuItem("Nöje");

			tNavPage.clickOnSubMenuItem("Idol");

			tNavPage.clickOnSubMenuItem("Musik");

			tNavPage.clickOnSubMenuItem("Spela");

			tNavPage.clickOnSubMenuItemLv2("Recensioner");
			tNavPage.clickOnSubMenuItemLv2("Krönikörer");
			tNavPage.clickOnSubMenuItemLv2("E-sport");


			tNavPage.clickOnSubMenuItem("Film");
			tNavPage.clickOnSubMenuItem("TV");
			tNavPage.clickOnSubMenuItem("Klick!");
			tNavPage.clickOnSubMenuItem("Melodifestivalen");
			tNavPage.clickOnSubMenuItem("Krönikörer");
			tNavPage.clickOnSubMenuItem("Så gick det sen");

			tNavPage.clickOnSubMenuItem("Rockbjörnen");
			tNavPage.clickOnSubMenuItemLv2("Justin Bieber");
			tNavPage.clickOnSubMenuItemLv2("Rösta");
			tNavPage.clickOnSubMenuItemLv2("Tidigare vinnare");
			tNavPage.clickOnSubMenuItemLv2("Om rockbjörnen");
			tNavPage.clickOnSubMenuItem("Hårdrock");

		}


		@Test
		@Ignore
		public void Ledare_Menu_and_Sub_menu ()throws Exception {
			WebDriver tDriver = driverWrapper.getDriver();
			System.out.println("Driver: " + tDriver);
			tNavPage = new AbBasicNav(tDriver);

			tNavPage.clickOnMenuItem("Ledare");

			tNavPage.clickOnSubMenuItem("Debatt");
			tNavPage.clickOnSubMenuItem("Debattskolan");
			tNavPage.clickOnSubMenuItem("Fusklappar");
		}

		@Test
		@Ignore
		public void Kultur_Menu_and_Sub_menu ()throws Exception {
			WebDriver tDriver = driverWrapper.getDriver();
			System.out.println("Driver: " + tDriver);
			tNavPage = new AbBasicNav(tDriver);

			tNavPage.clickOnMenuItem("Kultur");
			tNavPage.clickOnSubMenuItem("Böcker");
			tNavPage.clickOnSubMenuItem("Konst");
			tNavPage.clickOnSubMenuItem("Scen");
			tNavPage.clickOnSubMenuItem("Serier");
			tNavPage.clickOnSubMenuItem("Åsa Linderborg");
			tNavPage.clickOnSubMenuItem("Mejla oss");
		}

		@Test
		//@Ignore
		public void Plus_Menu ()throws Exception {
			WebDriver tDriver = driverWrapper.getDriver();
			System.out.println("Driver: " + tDriver);
			tNavPage = new AbBasicNav(tDriver);

			tNavPage.clickOnUserMenuItem("Plus");
			tNavPage.clickOnSubMenuItem("Erbjudanden");
			tNavPage.clickOnSubMenuItem("Frågor och svar om Plus");
			tNavPage.clickOnSubMenuItem("Min sida");

			tDriver.findElement(By.linkText("Tillbaka till Aftonbladet Plus")).click();

			tDriver.quit();


		}
	}