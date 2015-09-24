package se.redmind.rmtest.selenium.example;

import se.redmind.rmtest.selenium.framework.HTMLPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class RMNav extends HTMLPage {

    private static final Logger LOG = LoggerFactory.getLogger(RMNav.class);
    private WebDriver driver = getDriver();
    private Actions builder;

    /**
     * @param pDriver
     */
    public RMNav(WebDriver pDriver, String serverUrl) throws Exception {
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

    public WebDriver getMobileDriver() {
        return driver;
    }

    public String getCssSelector(String pText) {
        String textString;
        StringBuffer text = new StringBuffer(40);
        textString = text.append("a[href*='").append(pText).append("']")
                .toString();
        return textString;
    }

    public void clickOnMenu(String pMenuText) throws Exception {
        LOG.debug("Clicking: " + pMenuText);
        driver.findElement(By.cssSelector(getCssSelector(pMenuText))).click();
    }

    public void openMobileMenu() throws Exception {
        LOG.debug("Opening menu");
        driver.findElement(By.className("mobile-menu-control")).click();
    }

    public void clickOnSubmenu(String pMenuText, String pSubMenuText)
            throws Exception {
        Actions builder = new Actions(driver);
        builder.moveToElement(
                driver.findElement(By.cssSelector(getCssSelector(pMenuText))))
                .perform();
        LOG.debug("Hovering over " + pMenuText);
        driverFluentWait(20).until(ExpectedConditions.elementToBeClickable(By.className("sub-menu")));
        LOG.debug("Pressing " + pSubMenuText);
        driver.findElement(By.cssSelector(getCssSelector(pSubMenuText)))
                .click();
        driverFluentWait(20);
    }

    public void clickOnSubmenu(String pMenuText, String pSubMenuText,
                               String pSubSubMenuText) throws Exception {
        Actions builder = new Actions(driver);
        builder.moveToElement(
                driver.findElement(By.cssSelector(getCssSelector(pMenuText))))
                .perform();
        LOG.debug("Hovering over " + pMenuText);
        driverFluentWait(20).until(ExpectedConditions.elementToBeClickable(By.className("sub-menu")));
        builder.moveToElement(
                driver.findElement(By.cssSelector(getCssSelector(pSubMenuText))))
                .perform();
        LOG.debug("Hovering over " + pSubMenuText);
        driverFluentWait(20).until(ExpectedConditions.elementToBeClickable(By.className("sub-menu")));
        LOG.debug("Pressing " + pSubSubMenuText);
        driver.findElement(By.cssSelector(getCssSelector(pSubSubMenuText)))
                .click();
        driverFluentWait(20);
    }
}