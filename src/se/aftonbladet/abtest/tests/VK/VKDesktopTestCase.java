package se.aftonbladet.abtest.tests.VK;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import se.aftonbladet.abtest.navigation.VK.VKNav;
import se.aftonbladet.abtest.navigation.VK.VKParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-22
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */

@RunWith(Parallelized.class)
public class VKDesktopTestCase {
    private WebDriver tDriver;
    private VKNav tNavPage;
    private String startUrl = VKParams.getBaseUrl();

    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;

    public VKDesktopTestCase(final DriverNamingWrapper driverWrapper, final String driverDescription) {
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


    //TESTCASES START


    @Test
    //@Ignore
    public void ensure_login_successful() throws Exception {
        tDriver = driverWrapper.getDriver();
        System.out.println("Driver: " + tDriver);
        tNavPage = new VKNav(tDriver, startUrl);
        tNavPage.driverFluentWait(45).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='vk-logo vk-header-item']")));
        boolean b = tNavPage.VKIsLoggedIn();
        System.out.println("ensure_login_successful VKIsLoggedIn = " + b);
        if (b) {
            tNavPage.VKLogOut();
            tNavPage.driverFluentWait(25).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='sales-button-login']")));
        }

        tNavPage.VKLogIn("sian.abse@gmail.com", "aftonbladet.se!");
        tNavPage.assertPageContains(By.xpath("//*[@id='vk-start']"), "Översikt");
    }



    /*
    @Test
    //@Ignore
    public void ensure_login_button_clickable() throws Exception {
        tDriver = driverWrapper.getDriver();
        System.out.println("Driver: " + tDriver);
        tNavPage = new VKNav(tDriver, startUrl);
        tNavPage.driverFluentWait(45).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='vk-logo vk-header-item']")));

        tNavPage.VKLoginButtonClickTest();
        tNavPage.assertPageContains(By.xpath("//*[@id='credentials']"), "E-postadress");
    }
    */



}