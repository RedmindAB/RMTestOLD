package se.redmind.rmtest.selenium.framework;

import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

/**
 * @author petost
 */
public class HTMLPage {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HTMLPage.class);
    protected WebDriver driver;

    /**
     * @param driver WebDriver
     */
    public HTMLPage(final WebDriver pDriver) {
        this.driver = pDriver;
    }

    /**
     * @return WebDriver
     */
    public WebDriver getDriver() {
        return this.driver;
    }

    /**
     * @param timeoutInSeconds int
     * @return
     */
    private WebDriverWait driverWait(int timeoutInSeconds) {
        return new WebDriverWait(this.driver, timeoutInSeconds);
    }

    public FluentWait<WebDriver> driverFluentWait(int timeoutInSeconds) {
        FluentWait<WebDriver> fw = null;
        int i = 0;
        PrintStream quietErr;
        while(i < 10) {
            try {
                /*
                 * Stops prinouts of ExpectedConditions.findElement()
                 * This can be removed when printouts are removed
                 */
                Logger.getLogger("org.openqa.selenium.support.ui.ExpectedConditions").setLevel(Level.SEVERE);
                fw = new FluentWait<WebDriver>(this.driver)
                        .withTimeout(timeoutInSeconds, TimeUnit.SECONDS);
                fw.ignoring(WebDriverException.class, ClassCastException.class);
                fw.ignoring(NoSuchElementException.class);
                return fw;
            }
            catch(Exception e) {
                if(i >= 9) {
                    LOG.debug("driverFluentWait Failed attempt : " + i + "/n" + e);
                }
                i++;
            }
        }
        if(fw == null) {
            throw new WebDriverException("driverFluentWait failed after ten attempts");
        }
        else {
            return fw;
        }
    }

    /**
     * @param locator
     * @param timeoutInSeconds
     */
    public void driverWaitClickable(By locator, int timeoutInSeconds) {
        int i = 0;
        while(i < 10) {
            try {
                driverFluentWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));   // changed to driverFluentWait to ignore WebDriverExceptions braking the wait
                break;
            }
            catch(Exception e) {
                if(i >= 9) {
                    LOG.debug("driverWaitClickable exception: " + e);
                }
                i++;
            }
        }
    }

    /**
     * @param locator
     * @param timeoutInSeconds
     */
    public boolean driverFluentWaitForCondition(ExpectedCondition<?> condition, int timeoutInSeconds) {
        int i = 0;
        while(i < 10) {
            try {
                driverFluentWait(timeoutInSeconds).until(condition);   // changed to driverFluentWait to ignore WebDriverExceptions braking the wait
                return true;
            }
            catch(WebDriverException e) {
                LOG.debug("Caught a webdriveresception on driverFluentWaitForCondition try: " + i);
                LOG.debug("", e);
                i++;
            }
            catch(Exception e) {
                if(i >= 9) {
                    LOG.debug("This is another exception?" + e);
                }
                i++;
            }
        }
        return false;
    }

    /**
     * @param pBy
     * @param timeoutInSeconds
     */
    public void driverWaitElementPresent(By pBy, int timeoutInSeconds) {
        driverWait(timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(pBy));
    }

    /**
     * NB: might not work as expected: the predicate passed to until
     * seems to be called once, and only once.
     */
    protected void waitUntilDomReady() throws Exception {
        driverFluentWait(45).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(org.openqa.selenium.WebDriver webDriver) {
                JavascriptExecutor js = (JavascriptExecutor) webDriver;
                String result = (String) js.executeScript("return document.readyState");
                return "complete".equalsIgnoreCase(result);
            }
        });
    }

    public void assertPageTitle(String expPageTitle) throws Exception {
        LOG.debug("Try to assert page title: " + expPageTitle);
        String expPageTitleLow = expPageTitle.toLowerCase();
        String pageTitle = "--- Page not loaded ---";
        int i = 0;
        while(i < 10) {
            try {
                driverFluentWait(6).until(ExpectedConditions.titleContains(expPageTitle));
                pageTitle = driver.getTitle().toLowerCase();
                LOG.debug(">>>Compare to page title: " + pageTitle);  // pageTitle
                break;
            }
            catch(Exception e) {
                if(i >= 9) {
                    LOG.debug("pageTitle: " + pageTitle);
                    LOG.debug("----- AssertPageTitle Exception: " + e);
                }
                i = i + 1;
                Thread.sleep(50);
            }
        }
        assertTrue(pageTitle.contains(expPageTitleLow));
    }

    public boolean pageTitleContains(String expPageTitle) throws Exception {
        LOG.debug("Try to assert page title: " + expPageTitle);
        int i = 0;
        while(i < 10) {
            try {
                LOG.debug(">>>Compare to page title: " + driver.getTitle());
                boolean b = driver.getTitle().contains(expPageTitle);
                return b;
            }
            catch(Exception e) {
                if(i >= 9) {
                    LOG.debug("pageTitleContains exception: " + e);
                }
                i = i + 1;
                Thread.sleep(50);
            }
        }
        return false;
    }

    public boolean pageUrlContains(String articleId) throws Exception {
        LOG.debug("Try to assert page url: " + articleId);
        int i = 0;
        while(i < 10) {
            try {
                LOG.debug(">>>Compare to page url: " + articleId);
                boolean b = driver.getTitle().contains(articleId);
                return b;
            }
            catch(Exception e) {
                if(i >= 9) {
                    LOG.debug("pageTitleContains exception: " + e);
                }
                i = i + 1;
                Thread.sleep(50);
            }
        }
        return false;
    }

    public void assertPageContains(By locator, String expText) throws Exception {
        LOG.debug("Try to assert page contains: " + expText);
        int i = 0;
        while(i < 10) {
            try {
                driverFluentWait(1).until(ExpectedConditions.textToBePresentInElementLocated(locator, expText));
                driver.findElement(locator).getText().contains(expText);
                break;
            }
            catch(Exception e) {
                if(i >= 9) {
                    LOG.debug("----- assertPageContains Exception: " + e);
                }
                i = i + 1;
                Thread.sleep(50);
            }
        }
        assertTrue(driver.findElement(locator).getText().contains(expText));
    }

    public void spinnerClickBy(By path) throws Exception {
        LOG.debug("By: " + path);
        WebElement menuItem;
        int i = 0;
        while(i < 10) {
            try {
                menuItem = driver.findElement(path);
                menuItem.getLocation();
                driverFluentWait(1).until(ExpectedConditions.visibilityOf(menuItem));
                menuItem.getLocation();
                menuItem.click();
                break;
            }
            catch(Exception e) {
                if(i >= 9) {
                    LOG.debug("spinnerClickBy exception: " + e);
                }
                i = i + 1;
                Thread.sleep(50);
            }
        }
    }

    public void navigateStartUrl() {
        String bUrl = TestParams.getBaseUrl();
        driver.get(bUrl);
    }

    public String getTitle() {
        return driver.getTitle();
    }
    /**
     * @param fileName  Path to filename without extension. example: /tmp/thefilename
     */

}

