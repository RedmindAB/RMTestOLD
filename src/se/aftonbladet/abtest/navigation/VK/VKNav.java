package se.aftonbladet.abtest.navigation.VK;

import com.google.common.base.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import se.redmind.rmtest.selenium.framework.HTMLPage;

/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-22
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */

public class VKNav extends HTMLPage {
    private WebDriver driver = getDriver();

    public VKNav(WebDriver pDriver, String serverUrl) throws Exception {
        super(pDriver);
        driver = pDriver;

        int i = 0;
        while (i < 10) {
            try {
                driver.get(Objects.firstNonNull(serverUrl, "http://stage.viktklubb.aftonbladet.se/vk4"));
                driverFluentWait(6).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='vk-logo vk-header-item']")));
                break;
            }
            catch (Exception e) {
                System.out.println(i + " VKNav: " + e);
                i = i + 1;
                Thread.sleep(500);
            }
        }
    }

    /*
    public void VKLoginButtonClickTest() throws Exception {
        By loginButtonPath = By.xpath("//*[@id='sales-button-login']/a");

        //driverWaitElementPresent(By.xpath("//*[@id='sales-button-login']"), 15);
        driverWaitClickable(loginButtonPath, 10);
        spinnerClickBy(loginButtonPath);
        driverFluentWait(10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='identifier']")));
        System.out.println("*********************************************************** SPiD Login Page Reached");
    }
    */

    //-------------------------LOGIN/LOGOUT NAVIGATION----------------------------

    public void VKLogIn(String userName, String passWord) throws Exception {
        By loginButtonPath = By.xpath("//*[@id='login-link']");
        By userNameFieldPath = By.xpath("//*[@id='identifier']");
        By passWordFieldPath = By.xpath("//*[@id='password']");
        By spidLoginButtonPath = By.xpath("//*[@value='Logga in']");

        driverWaitElementPresent(By.xpath("//*[@id='login-link']"), 15);
        spinnerClickBy(loginButtonPath);
        driverWaitClickable(userNameFieldPath, 10);
        driver.findElement(userNameFieldPath).sendKeys(userName);
        driver.findElement(passWordFieldPath).sendKeys(passWord);
        spinnerClickBy(spidLoginButtonPath);
        //driverFluentWait(10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='vk-start']")));
        driverWaitClickable(By.xpath("//*[@id='vk-start']"), 30);
    }

    public void VKLogOut() throws Exception {
        By userNameDropDown = By.xpath("//*[@id='vk-user-display-name-top']");
        By logOutButtonPath = By.xpath("//*[@id='vk-profile-logout-link']");

        spinnerClickBy(userNameDropDown);
        driverWaitClickable(logOutButtonPath, 10);
        System.out.println("************************************************************ Logout initiated *******");
        spinnerClickBy(logOutButtonPath);
        System.out.println("************************************************************ Logout completed *******");

    }

    public boolean VKIsLoggedIn() throws Exception {
        boolean isLoggedIn = false;
        int i = 0;
        while (i < 10) {

            try {
                WebElement linkElement = driver.findElement(By.xpath("//*[@id='vk-profile-logout-link']"));
                String linkText = linkElement.getText();
                System.out.println("linkText: " + linkText);
                isLoggedIn = linkText.contentEquals("Logga ut >>>");
                System.out.println("****************************************************** VKIsLoggedIn " + isLoggedIn);
                return isLoggedIn;
            }
            catch (Exception e) {
                System.out.println("VKIsLoggedIn exception: " + e);
                i = i + 1;
                Thread.sleep(50);
            }
        }

        return isLoggedIn;
    }

    public void VKLoginIfLoggedOut() throws Exception {
        boolean isLoggedIn = VKIsLoggedIn();
        if(!isLoggedIn) {
            VKLogIn("sian.abse@gmail.com", "aftonbladet.se!");
        }

    }


    //-------------------------2014 SALESPAGES NAVIGATION----------------------------

    public void VKBecomeMemberBtn() throws Exception {
        By vkBecomeMemberBtn = By.xpath("//*[@class='btn btn-primary']");
        spinnerClickBy(vkBecomeMemberBtn);
    }

    public void VKMemberIncludesBtn() throws Exception {
        By vkMemberIncludesBtn = By.xpath("//*[@class='btn btn-default']");
        spinnerClickBy(vkMemberIncludesBtn);
    }

    public void

}