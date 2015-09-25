package se.redmind.rmtest.selenium.example;

import se.redmind.rmtest.selenium.framework.HTMLPage;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class RmMobileNav extends HTMLPage {

    private static final Logger LOG = LoggerFactory.getLogger(RmMobileNav.class);

    Actions builder;

    /**
     * @param pDriver
     */
    public RmMobileNav(WebDriver pDriver, String serverUrl) throws Exception {
        super(pDriver);
        driver = pDriver;
        int i = 0;
        while(i < 10) {
            try {
                driver.get(Objects.firstNonNull(serverUrl,
                        "http://www.redmind.se"));
                break;
            }
            catch(Exception e) {
                LOG.debug(i + " AbMobileNav: " + e);
                i = i + 1;
                Thread.sleep(500);
            }
        }
    }

    public void openMobileMenu() {
        driverWaitElementPresent(By.className("mobile-menu-control"), 60);
        driver.findElement(By.className("mobile-menu-control")).click();
        LOG.debug("Opening menu");
        driverFluentWait(20).until(ExpectedConditions.presenceOfElementLocated(By.id("menu-main-menu-1")));
    }

    public void openTpi(String Menu, String SubMenu) throws Exception {
        driverFluentWait(20).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(Menu)));
        builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.linkText(Menu))).perform();
        LOG.debug("Pressing " + Menu);
        driver.findElement(By.linkText(SubMenu)).click();
    }

    public void openManag(String pMenu, String pSubMenu) throws Exception {
        driverFluentWait(20).until(ExpectedConditions.presenceOfElementLocated(By.linkText(pMenu)));
        builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.linkText(pMenu))).perform();
        LOG.debug("Hovering over " + pMenu);
        LOG.debug("Pressing " + pSubMenu);
        driver.findElement(By.linkText(pSubMenu)).click();
    }

    public void openRyk(String pText, String pSubText) throws Exception {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.linkText(pText))).perform();
        LOG.debug("Pressing " + pSubText);
        driver.findElement(By.linkText(pSubText)).click();
    }

    public void openClAc(String Text, String SubText) throws Exception {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.linkText(Text))).perform();
        LOG.debug("Pressing " + SubText);
        driver.findElement(By.linkText(SubText)).click();
    }

    public void openKTj(String menu, String subMenu1, String subMenu2)
            throws Exception {
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.linkText(menu))).perform();
        builder.moveToElement(driver.findElement(By.linkText(subMenu1)))
                .perform();
        Thread.sleep(500L);
        driver.findElement(By.linkText(subMenu2)).click();
    }

    public void clickOnMobileMenu(String Text, String SubText) {
        driverWaitClickable(By.linkText(Text), 20);
        builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.linkText(Text))).perform();
        LOG.debug("Hovering over " + Text);
        driverWaitClickable(By.linkText(SubText), 20);
        driver.findElement(By.linkText(SubText)).click();
        LOG.debug("Pressing " + SubText);
        driverWaitElementPresent(By.className("mobile-menu-control"), 60);
    }

    public void clickOnMobileMenu(String Text, String SubText, String SubSubText) {
        driverWaitClickable(By.linkText(Text), 20);
        builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.linkText(Text))).perform();
        LOG.debug("Hovering over " + Text);
        driverWaitClickable(By.linkText(SubText), 20);
        builder.moveToElement(driver.findElement(By.linkText(SubText))).perform();
        LOG.debug("Hovering over " + SubText);
        driverWaitClickable(By.linkText(SubText), 20);
        driver.findElement(By.linkText(SubSubText)).click();
        LOG.debug("Pressing " + SubSubText);
        driverWaitElementPresent(By.className("mobile-menu-control"), 60);
    }

    public void clickOnAndroidMenu(String text, String subText) throws InterruptedException {
        driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.linkText(text)), 20);
        driver.findElement(By.linkText(text)).click();
        driver.findElement(By.linkText(subText)).click();
    }

    public void clickOnAndroidMenu(String text, String subText, String subSubText) throws InterruptedException {
        driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.linkText(text)), 20);
        driver.findElement(By.linkText(text)).click();
        driver.findElement(By.linkText(subText)).click();
        driver.findElement(By.linkText(subSubText)).click();
    }
}
