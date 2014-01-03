package se.remind.rmtest.selenium.tests.desktop;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import se.remind.rmtest.selenium.framework.AbBasicNav;

/**
 * Created with IntelliJ IDEA.
 * User: redben
 * Date: 2013-07-17
 * Time: 09:36
 */
public class ChromeNavMenuTest {

    @org.junit.Test

    public void NavTest() throws Exception{
        System.setProperty("webdriver.chrome.driver", "/Applications/Google Chrome.app/Contents/chromedriver");
        WebDriver tDriver = new ChromeDriver();

        AbBasicNav tNavPage = new AbBasicNav(tDriver);
        //tDriver.get("http://www.aftonbladet.se");

        //Loggin with Abse Test Account

            tNavPage.clickOnMenuItem("Logga in");

        Thread.sleep(1000);
        tDriver.findElement(By.name("search")).clear();
        tDriver.findElement(By.name("search")).sendKeys("fb.abse@gmail.com");
        tDriver.findElement(By.name("password")).sendKeys("aftonbladet.se");
        tDriver.findElement(By.id("remember_label")).submit();


        //Sport and sub-menu
        tNavPage.clickOnMenuItem("Sport");
        tNavPage.assertPageTitle("Sport");


                tNavPage.clickOnSubMenuItem("LIVE/Mål för mål");
                //tNavPage.assertPageTitle("mål för mål");

                        tNavPage.clickOnSubMenuItemLv2("Fotboll Live");
                        tNavPage.clickOnSubMenuItemLv2("Allsvenskan Live");
                        tNavPage.clickOnSubMenuItemLv2("Leifby Live");
                        tNavPage.clickOnSubMenuItemLv2("Elitserien live");
                        tNavPage.clickOnSubMenuItemLv2("HockeyAllsvenskan Live");
                        tNavPage.clickOnSubMenuItemLv2("F1-bevakaren");

                        tNavPage.clickOnSubMenuItem("Resultat/Tabeller");
                        // tNavPage.assertPageTitle("Resultat/Tabeller");


                tNavPage.clickOnSubMenuItem("Fotboll");
                //tNavPage.assertPageTitle("Fotboll");

                        tNavPage.clickOnSubMenuItemLv2("Landslaget");
                        tNavPage.clickOnSubMenuItemLv2("Damlandslaget");
                        tNavPage.clickOnSubMenuItemLv2("VM-kvalet");
                        tNavPage.clickOnSubMenuItemLv2("Allsvenskan");
                        tNavPage.clickOnSubMenuItemLv2("Damallsvenskan");
                        tNavPage.clickOnSubMenuItemLv2("Premier League");
                        tNavPage.clickOnSubMenuItemLv2("La Liga");
                        tNavPage.clickOnSubMenuItemLv2("Serie A");
                        tNavPage.clickOnSubMenuItemLv2("Bundesliga");
                        tNavPage.clickOnSubMenuItemLv2("Ligue 1");
                        tNavPage.clickOnSubMenuItemLv2("Zlatan");
                        tNavPage.clickOnSubMenuItemLv2("Guldbollen");


                tNavPage.clickOnSubMenuItem("EM 2013");
                //tNavPage.assertPageTitle("Em 2013");

                tNavPage.clickOnSubMenuItem("Hockey");
                //tNavPage.assertPageTitle("Hockey");

                        tNavPage.clickOnSubMenuItemLv2("Hockey-VM");
                        tNavPage.clickOnSubMenuItemLv2("Tre Kronor");
                        tNavPage.clickOnSubMenuItemLv2("Damkronorna");
                        tNavPage.clickOnSubMenuItemLv2("Elitserien");
                        tNavPage.clickOnSubMenuItemLv2("Hockeyallsvenskan");
                        tNavPage.clickOnSubMenuItemLv2("Riksserien");
                        tNavPage.clickOnSubMenuItemLv2("NHL");
                        tNavPage.clickOnSubMenuItemLv2("KHL");

                tNavPage.clickOnSubMenuItem("Motor");
                //tNavPage.assertPageTitle("Motor");

                        tNavPage.clickOnSubMenuItemLv2("Formel 1");
                        tNavPage.clickOnSubMenuItemLv2("Speedway");

                tNavPage.clickOnSubMenuItem("Trav 365");
                //tNavPage.assertPageTitle("Trav 365");

                        tNavPage.clickOnSubMenuItemLv2("Startlistor");
                        tNavPage.clickOnSubMenuItemLv2("V86-tips");
                        tNavPage.clickOnSubMenuItemLv2("V75-tips");
                        tNavPage.clickOnSubMenuItemLv2("V65-tips");
                        tNavPage.clickOnSubMenuItemLv2("V64-tips");
                        tNavPage.clickOnSubMenuItemLv2("V4-tips");
                        tNavPage.clickOnSubMenuItemLv2("DD-tips");
                        tNavPage.clickOnSubMenuItemLv2("Åke Svanstedt");
                        tNavPage.clickOnSubMenuItemLv2("Björn Goop");
                        tNavPage.clickOnSubMenuItemLv2("Stig H:s V86-tips");
                        tNavPage.clickOnSubMenuItemLv2("Stig H:s V75-tips");
                        tNavPage.clickOnSubMenuItemLv2("Miljonklubben");
                        tNavPage.clickOnSubMenuItemLv2("Intervjuer till V86");
                        tNavPage.clickOnSubMenuItemLv2("Intervjuer till V75");
                        tNavPage.clickOnSubMenuItemLv2("Lutfi Kolgjini");
                        tNavPage.clickOnSubMenuItemLv2("Challes tips");
                        tNavPage.clickOnSubMenuItemLv2("Micke Nybrink");

                tNavPage.clickOnSubMenuItem("Spel");
                //tNavPage.assertPageTitle("Spel");

                        tNavPage.clickOnSubMenuItemLv2("Dagens Bästa Spel");
                        tNavPage.clickOnSubMenuItemLv2("Bomben & Lången");
                        tNavPage.clickOnSubMenuItemLv2("Tipset");
                        tNavPage.clickOnSubMenuItemLv2("Power Play");
                        tNavPage.clickOnSubMenuItemLv2("Långenlistan");

                tNavPage.clickOnSubMenuItem("Poker");
                //tNavPage.assertPageTitle("Poker");

                tNavPage.clickOnSubMenuItem("S24");
                //tNavPage.assertPageTitle("S24");

                        tNavPage.clickOnSubMenuItemLv2("S24 Arkiv");
                        tNavPage.clickOnSubMenuItemLv2("Så här gör du");
                        tNavPage.clickOnSubMenuItemLv2("Frågor och svar");
                        tNavPage.clickOnSubMenuItemLv2("Kontakta oss");

                tNavPage.clickOnSubMenuItem("Mer sport");
                //tNavPage.assertPageTitle("Mer sport");

        tNavPage.clickOnMenuItem("Nöje");

                tNavPage.clickOnSubMenuItem("Melodifestivalen");

                tNavPage.clickOnSubMenuItem("Musik");

                tNavPage.clickOnSubMenuItem("Spela");

                        tNavPage.clickOnSubMenuItemLv2("Recensioner");
                        tNavPage.clickOnSubMenuItemLv2("Krönikörer");
                        tNavPage.clickOnSubMenuItemLv2("E-sport");
                        //tNavPage.clickOnSubMenuItemLv2("Speltuben");
                        //tNavPage.clickOnSubMenuItemLv2("Blog 'em up");

                tNavPage.clickOnSubMenuItem("Film");
                tNavPage.clickOnSubMenuItem("TV");
                tNavPage.clickOnSubMenuItem("Klick!");
                tNavPage.clickOnSubMenuItem("Fokus");
                tNavPage.clickOnSubMenuItem("Krönikörer");
                tNavPage.clickOnSubMenuItem("Så gick det sen");

                tNavPage.clickOnSubMenuItem("Rockbjörnen");

                        tNavPage.clickOnSubMenuItemLv2("Justin Bieber");
                        tNavPage.clickOnSubMenuItemLv2("Rösta");
                        tNavPage.clickOnSubMenuItemLv2("Tidigare vinnare");
                        tNavPage.clickOnSubMenuItemLv2("Om rockbjörnen");
                        //tNavPage.clickOnSubMenuItemLv2("Blogg");
                        //tNavPage.clickOnSubMenuItemLv2("Skapa TV");

        tNavPage.clickOnSubMenuItem("Hårdrock");


        tNavPage.clickOnMenuItem("Ekonomi");

                tNavPage.clickOnSubMenuItem("Shopping");
                tNavPage.clickOnSubMenuItem("Bredband/telefoni");
                tNavPage.clickOnSubMenuItem("Boende");
                tNavPage.clickOnSubMenuItem("Spara/Pension");
                tNavPage.clickOnSubMenuItem("El");
                tNavPage.clickOnSubMenuItem("Försäkringar");
                tNavPage.clickOnSubMenuItem("Lån");
                tNavPage.clickOnSubMenuItem("Familj");


        tNavPage.clickOnMenuItem("Ledare");

                tNavPage.clickOnSubMenuItem("Debatt");
                //tNavPage.clickOnSubMenuItem("Karin Magnusson");
                tNavPage.clickOnSubMenuItem("Debattskolan");
                tNavPage.clickOnSubMenuItem("Fusklappar");
                tNavPage.clickOnSubMenuItem("Rebecca Weidmo Uvell");
                tNavPage.clickOnSubMenuItem("Redaktionens länktips");

        tNavPage.clickOnMenuItem("Kultur");
                tNavPage.clickOnSubMenuItem("Böcker");
                tNavPage.clickOnSubMenuItem("Konst");
                tNavPage.clickOnSubMenuItem("Scen");
                tNavPage.clickOnSubMenuItem("Serier");
                tNavPage.clickOnSubMenuItem("Åsa Linderborg");
                tNavPage.clickOnSubMenuItem("Mejla oss");

        tNavPage.clickOnMenuItem("Plus");
                tNavPage.clickOnSubMenuItem("Erbjudanden");
                tNavPage.clickOnSubMenuItem("Frågor och svar om Plus");
                tNavPage.clickOnSubMenuItem("Min sida");

        tDriver.findElement(By.linkText("Tillbaka till Aftonbladet Plus")).click();

        System.out.println("Passed!");
        System.out.println("Closing drivers!");
        tDriver.quit();


    }
}
