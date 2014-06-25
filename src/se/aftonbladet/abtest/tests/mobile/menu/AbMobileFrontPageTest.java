package se.aftonbladet.abtest.tests.mobile.menu;


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

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: oskeke
 */
@RunWith(Parallelized.class)
public class AbMobileFrontPageTest {
    private WebDriver tDriver;
    private AbMobileNav tNavPage;
    private String startUrl = TestParams.getBaseUrl();

    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;

    public AbMobileFrontPageTest(final DriverNamingWrapper driverWrapper, final String driverDescription) {
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
    public void ensure_mobile_navigates_to_clicked_article() throws Exception {
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
    public void ensure_mobile_frontpage_elements_present() throws Exception {
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
