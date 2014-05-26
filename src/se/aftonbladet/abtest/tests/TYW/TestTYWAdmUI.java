package se.aftonbladet.abtest.tests.TYW;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import se.aftonbladet.abtest.navigation.TYW.TYWAdmUINav;
import org.openqa.selenium.support.ui.ExpectedConditions;
import se.aftonbladet.abtest.navigation.TYW.TYWAdmUIParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-20
 * Time: 10:28
 * To change this template use File | Settings | File Templates.
 */

@RunWith(Parallelized.class)
public class TestTYWAdmUI {
    private WebDriver tDriver;
    private TYWAdmUINav tNavPage;
    private String startUrl = TYWAdmUIParams.getBaseUrl();

    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;

    public TestTYWAdmUI(final DriverNamingWrapper driverWrapper, final String driverDescription) {
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

    //TESTCASES

    @Test
    //@Ignore
    public void ensure_login_successful() throws Exception {
        tDriver = driverWrapper.getDriver();
        System.out.println("Driver: " + tDriver);
        tNavPage = new TYWAdmUINav(tDriver, startUrl);
        tNavPage.driverFluentWait(45).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='navbar-brand']")));
        boolean b = tNavPage.TYWIsLoggedIn();
        System.out.println("Ensure_login_successful IsLoggedIn = " + b);
        if (b) {
            tNavPage.TYWBackendLogOut();
            //tNavPage.driverWaitClickable(By.xpath("/html/body/div/div[2]/ul[2]/li/a"), 10);
            tNavPage.driverFluentWait(25).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[2]/ul[2]/li/a[text()='Login']")));
        }

        tNavPage.TYWBackendLogIn("tywadmin", "adminpass");
        tNavPage.assertPageContains(By.xpath("/html/body/div/div[2]/ul[2]/li[2]/a"), "Logout");

    }


    @Test
    //Ignore
    public void ensure_top_menu_navigation_leads_to_sub_menu_navigation() throws Exception {
        tDriver = driverWrapper.getDriver();
        System.out.println("Driver: " + tDriver);
        tNavPage = new TYWAdmUINav(tDriver, startUrl);
        tNavPage.driverFluentWait(30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/a")));
        tNavPage.TYWLoginIfLoggedOut();

        tNavPage.NavTopMenu("Importer");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h3"), "Available importers");

        tNavPage.NavTopMenu("Category mappings");
        tNavPage.NavSubMenu("Category mappings", "Show category tree");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Category tree");

        tNavPage.NavTopMenu("Category mappings");
        tNavPage.NavSubMenu("Category mappings", "Show category mappings");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Existing Category Mappings");
        tNavPage.NavTopMenu("Category mappings");
        tNavPage.NavSubMenu("Category mappings", "Show external categories");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Failed Category Mappings");

        tNavPage.NavTopMenu("Color mappings");
        tNavPage.NavSubMenu("Color mappings", "Show color categories");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Color categories");
        tNavPage.NavTopMenu("Color mappings");
        tNavPage.NavSubMenu("Color mappings", "Show color mappings");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Existing Color Mappings");
        tNavPage.NavTopMenu("Color mappings");
        tNavPage.NavSubMenu("Color mappings", "Show external colors");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Failed Color Mappings");

        tNavPage.NavTopMenu("Size mappings");
        tNavPage.NavSubMenu("Size mappings", "Show size groups");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Size groups");
        tNavPage.NavTopMenu("Size mappings");
        tNavPage.NavSubMenu("Size mappings", "Show size mappings");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Existing Size Mappings");
        tNavPage.NavTopMenu("Size mappings");
        tNavPage.NavSubMenu("Size mappings", "Show external sizes");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Failed Size Mappings");

        tNavPage.NavTopMenu("Brand mappings");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Brand Mappings");

        tNavPage.NavTopMenu("Config");
        tNavPage.NavSubMenu("Config", "Parser config");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Parser configuration");

        tNavPage.NavTopMenu("Config");
        tNavPage.NavSubMenu("Config", "Services info");
        tNavPage.assertPageContains(By.xpath("/html/body/div[2]/h1"), "Importer services");

        tNavPage.NavTopMenu("Config");
        tNavPage.NavSubMenu("Config", "Users");
        tNavPage.assertPageContains(By.xpath("//*[@id='userTable']"), "Username");
    }

    @Test
    //@Ignore
    public void ensure_creation_of_new_top_node_in_category_tree() throws Exception {
        tDriver = driverWrapper.getDriver();
        System.out.println("Driver: " + tDriver);

    }


}
