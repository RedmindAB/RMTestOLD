package se.redmind.rmtest.selenium.example;

import se.redmind.rmtest.selenium.framework.HTMLPage;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleNav extends HTMLPage {
    private WebDriver driver = getDriver();

    private static final Logger LOG = LoggerFactory.getLogger(GoogleNav.class);

    /**
     * @param pDriver
     */
    public GoogleNav(WebDriver pDriver) throws Exception {
        super(pDriver);
        driver = pDriver;
        int i = 0;
        while(i < 10) {
            try {
                driver.get("http://www.google.se");
                break;
            }
            catch(Exception e) {
                LOG.debug("", e);
            }
        }
    }

    public WebElement searchBox() {
        By searchInputField = By.name("q");
        driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(searchInputField));
        return driver.findElement(searchInputField);
    }

    public void searchForString(String searchString) throws Exception {
        By searchResult = By.id("ires");
        WebElement searchbox = searchBox();
        searchbox.sendKeys(searchString);
        searchbox.sendKeys(Keys.RETURN);
        driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(searchResult));
    }
}
