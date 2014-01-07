package se.aftonbladet.abtest.navigation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.redmind.rmtest.selenium.framework.HTMLPage;




/**
 * @author petost
 *
 */
public class AbBasicNav extends HTMLPage {
    private WebDriver mDriver;
    private By mLoginDiv = By.id("abHeaderUserLogin");

    /**
     * @param pDriver WebDriver
     */
    public AbBasicNav(final WebDriver pDriver) {
        super(pDriver);
        mDriver = pDriver;
        mDriver.get("http://www.aftonbladet.se/");

    }

    public WebElement getLoginDiv(){
        return mDriver.findElement(mLoginDiv);
    }

    public boolean isLoggedIn() {
        driverWaitElementPresent(mLoginDiv, 2);
        boolean tIsLoggedin = false;
        if (!getLoginDiv().getText().equals("Logga In")) {
            tIsLoggedin=true;
        }
        return tIsLoggedin;
    }

    public void Login (String name, String password) throws Exception{

        clickOnMenuItem("Logga in");

        int i = 0;
        while (i < 10) {
            try {
                mDriver.findElement(By.name("identifier")).clear();
                mDriver.findElement(By.name("identifier")).sendKeys(name);
                mDriver.findElement(By.name("password")).sendKeys(password);
                mDriver.findElement(By.id("remember_label")).submit();
                break;
            }
            catch (Exception e) {
                System.out.println("Login exception: " + e);
                i = i + 1;
                Thread.sleep(50);
            }
        }
    }

    public void clickOnMenuItem(String pMenuText) throws Exception {
        By byItemPath =  By.xpath("//a[text()='" + pMenuText + "']");

        driverFluentWait(10).until(ExpectedConditions.elementToBeClickable(byItemPath));
        spinnerClickBy(byItemPath);
    }

    public void clickOnSubMenuItem(String pSubMenuText) throws Exception {
        By byItemPath = By.xpath("//*[@id='abInnerBody']/div[@class='abContent']/nav[@class='abSubNav abSubMenuLevel2']/ul[@class='abHList clearfix']/li/a[text()='" + pSubMenuText + "']");

        driverFluentWait(10).until(ExpectedConditions.elementToBeClickable(byItemPath));
        spinnerClickBy(byItemPath);


    }

    public void clickOnSubMenuItemLv2(String pSubMenuTextLv2) throws Exception {

        By byItemPath = By.xpath("//*[@id='abInnerBody']/div[@class='abContent']/nav[@class='abSubNav abSubMenuLevel3']/ul[@class='abHList clearfix']/li/a[text()='" + pSubMenuTextLv2 + "']");

        driverFluentWait(10).until(ExpectedConditions.elementToBeClickable(byItemPath));
        spinnerClickBy(byItemPath);


    }

    public void waitForPageToLoad(int pWaitTime){
//      mMenu = mDriver.findElement(By.xpath("//*[@id='abInnerBody']/div[3]/nav[1]/ul[1]/li[3]/a"));
//      WebElement Sport = tDriver.findElement(By.xpath("//a[text()='Sport']"));
//      driverWait(2).until(ExpectedConditions.titleCo7ntains(title));

//      driverWait(2).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Stora Journalistpriset 2012.']")));
    }

}
