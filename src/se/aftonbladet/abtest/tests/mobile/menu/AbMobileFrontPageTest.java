package se.aftonbladet.abtest.tests.mobile.menu;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.aftonbladet.abtest.navigation.AbMobileNav;
import se.remind.rmtest.selenium.framework.TestParams;
import se.remind.rmtest.selenium.grid.DriverNamingWrapper;
import se.remind.rmtest.selenium.grid.DriverProvider;

/**
 * User: oskeke
 */
@RunWith(JUnitParamsRunner.class)
public class AbMobileFrontPageTest {
    private WebDriver tDriver;
    private AbMobileNav tNavPage;
    private String startUrl = TestParams.getBaseUrl();


    private Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.ANDROID, Platform.MAC);
    }

    @Test
    //@Ignore
    @Parameters(method = "getDrivers")
    public void ensure_mobile_navigates_to_clicked_article(DriverNamingWrapper driverWrapper) throws Exception {
        tDriver = driverWrapper.getDriver();
        tNavPage = new AbMobileNav(tDriver, startUrl);
        tNavPage.driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));

        // TODO: Add a proper assert after navigation to first article, requires test environment with stable front page and articles.
        tNavPage.clickFirstTeaser();
        tNavPage.navigateBack();
        tNavPage.assertPageTitle("Aftonbladet: Sveriges nyhetskälla och mötesplats");

        /*
        tNavPage.clickTeaserByArticleId("17491063");
        tNavPage.assertPageContains(By.xpath("//p"), "avfolkningsbygden");
        */
    }

    @Test
    @Parameters(method = "getDrivers")
    public void ensure_mobile_frontpage_elements_present(DriverNamingWrapper driverWrapper) throws Exception {
        tDriver = driverWrapper.getDriver();
        tNavPage = new AbMobileNav(tDriver, startUrl);
        tNavPage.driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));

        tNavPage.assertPageContainsElementByClassName("abAdPoll");
        tNavPage.assertPageContainsElementByClassName("abBox");
        tNavPage.assertPageContainsElementByClassName("abFooter");
        tNavPage.assertPageContains(By.xpath("//footer/div[@class='abBottomRow']/p"), "Tjänstgörande mobilredaktör:");
        tNavPage.assertPageContainsElementByClassName("abStatLatestArticles");
        tNavPage.assertNumberOfElementsByXpath("//div[@class='abBoxNoPad abStatLatestArticles']/div[1]/a", 5);

        //tNavPage.assertPageContains(By.xpath("//div[@class='abBoxNoPad']/div[@class='abStreamer']/span[@class='abStreamerInner']"), "Fler nyheter");
        //tNavPage.assertPageContains(By.cssSelector("div[class='abBoxNoPad abToggleTarget']:div[class='abStreamer abThemeBgGradient':span)"), "Fler nyheter");
    }
}
