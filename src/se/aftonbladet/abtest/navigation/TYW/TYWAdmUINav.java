package se.aftonbladet.abtest.navigation.TYW;

import com.google.common.base.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import se.redmind.rmtest.selenium.framework.HTMLPage;

/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-20
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class TYWAdmUINav extends HTMLPage {
    private WebDriver driver = getDriver();

    public TYWAdmUINav(WebDriver pDriver, String serverUrl) throws Exception {
        super(pDriver);
        driver = pDriver;

        int i = 0;
        while (i < 10) {
            try {
                driver.get(Objects.firstNonNull(serverUrl, "http://www.stage3.abse.aftonbladet.se:8088"));
                driverFluentWait(6).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*/img[@class='logo']")));
                break;
            }
            catch (Exception e) {
                System.out.println(i + " TYWAdmUINav: " + e);
                i = i + 1;
                Thread.sleep(500);
            }
        }
    }


    public void TYWBackendLogIn(String userName, String passWord) throws Exception {
        By loginButtonPath = By.xpath("//*[text()='Login']");
        By userNamePath = By.xpath("//*[@type='text']");
        By passWordPath = By.xpath("//*[@type='password']");
        By submitPath = By.xpath("//*[@type='submit']");

        spinnerClickBy(loginButtonPath);
        driverWaitClickable(userNamePath, 10);
        driver.findElement(userNamePath).sendKeys(userName);
        driver.findElement(passWordPath).sendKeys(passWord);
        spinnerClickBy(submitPath);
        driverFluentWait(10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='navbar-brand']")));

    }

    public void TYWBackendLogOut() throws Exception {
        By logoutButtonPath = By.xpath("/html/body/div/div[2]/ul[2]/li[2]/a");                                                  //<- Replace with a shorter xpath once possible (after ID-tagging)
        driverWaitClickable(logoutButtonPath, 10);
        System.out.println("**************************************************** Logout initiated. ****************");
        spinnerClickBy(logoutButtonPath);
        driverWaitElementPresent(By.xpath("//*/input[@type='text']"), 10);
        System.out.println("**************************************************** Logout completed. ****************");
    }

    public boolean TYWIsLoggedIn() throws Exception {
        boolean isLoggedIn = false;
        int i = 0;
        while (i < 10) {

            try {
                //System.out.println("TYWIsLoggedIn first line in try catch *****************WEeeeeeeeeeeeeeeeeeee");
                WebElement linkElement = driver.findElement(By.xpath("/html/body/div/div[2]/ul[2]/li[2]/a"));                   //<- Replace with a shorter xpath once possible (after ID-tagging)
                String linkText = linkElement.getText();
                System.out.println("linkText: " + linkText);
                isLoggedIn = linkText.contentEquals("Logout");
                System.out.println("*************************************************************** TYWIsLoggedIn " + isLoggedIn);
                return isLoggedIn;
            }
            catch (Exception e) {
                System.out.println("TYWIsLoggedIn exception: " + e);
                i = i + 1;
                Thread.sleep(50);
            }
        }

        return isLoggedIn;
    }

    public void TYWLoginIfLoggedOut() throws Exception {
        boolean isLoggedIn = TYWIsLoggedIn();
        if(!isLoggedIn) {
            TYWBackendLogIn("tywadmin", "adminpass");
        }

    }

    //NAVIGATE THE TOP MENU (EXCL. Importer)

    public void NavTopMenu(String topMenuNav) throws Exception {
        By menuItemPath = By.xpath("//div[@id='bs-example-navbar-collapse-1']/ul/li/a[text()='" + topMenuNav + "']");
        spinnerClickBy(menuItemPath);
    }

    public void NavSubMenu(String topMenuNav, String subMenuNav) throws Exception {
        By menuItemPath = By.xpath("//div[@id='bs-example-navbar-collapse-1']/ul/li/a[text()='" + topMenuNav + "']/../ul[@class='dropdown-menu']/li/a[text()='" + subMenuNav + "']");
        spinnerClickBy(menuItemPath);
    }

    //CHECKBOXES BY IMPORTER, RUN IMPORTER

    public void ImportTick(String storeName) throws Exception {
        By storeCheckBox = By.xpath("//*/input[@value='" + storeName + "']");
        spinnerClickBy(storeCheckBox);
    }

    public void RunImporter() throws Exception {
        By runImportButton = By.xpath("//*input[@type='submit']");
        spinnerClickBy(runImportButton);
    }


}
