package se.redmind.rmtest.selenium.framework;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

/**
 * @author petost
 *
 */
public class HTMLPage {
    private WebDriver driver;
    
    /**
     * @param driver WebDriver
     */
    public HTMLPage(final WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * @return WebDriver
     */
    public final WebDriver getDriver() {
        return driver;
    }    
    
    /**
     * @param timeoutInSeconds int
     * @return
     */
    public WebDriverWait driverWait(int timeoutInSeconds) {
        return new WebDriverWait(driver, timeoutInSeconds);
    }
    
    public FluentWait<WebDriver> driverFluentWait(int timeoutInSeconds) {
    	FluentWait fw = null;
        int i = 0;

        while (i<10) {
            try {
                fw = new FluentWait(driver)
                .ignoring(WebDriverException.class, ClassCastException.class);

                return fw;
            }
            catch(Exception e){
                System.out.println("driverFluentWait: " + e);
                i++;
            }
        }
        if (fw==null){
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
    public void driverWaitClickable(By locator, int timeoutInSeconds){
        int i = 0;
        while (i < 10) {
            try {
                driverFluentWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));   // changed to driverFluentWait to ignore WebDriverExceptions braking the wait
                break;
            }
            catch (Exception e) {
                System.out.println("driverWaitClickable exception: " + e);
                i++;
            }
        }

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
			
		
		
		/*
		Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
				.withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class);

		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(org.openqa.selenium.WebDriver webDriver) {
				JavascriptExecutor js = (JavascriptExecutor) webDriver;
				String result = (String) js.executeScript("return document.readyState");
				return "complete".equalsIgnoreCase(result);
			}
		});
		*/
	}
	

	public void assertPageTitle(String expPageTitle) throws Exception {
		System.out.println("Try to assert page title: " + expPageTitle);
		
		String expPageTitleLow = expPageTitle.toLowerCase();
		String pageTitle = "--- Page not loaded ---";
		int i = 0;
		while (i < 10) {
			try {
                driverFluentWait(6).until(ExpectedConditions.titleContains(expPageTitle));
				pageTitle = driver.getTitle().toLowerCase();
				System.out.println(">>>Compare to page title: " + pageTitle);  // pageTitle
				break;
			}
			catch (Exception e) {
				System.out.println("pageTitle: " + pageTitle);
				System.out.println("----- AssertPageTitle Exception: " + e);
				i = i + 1;
				Thread.sleep(50);
			}				
		}
		assertTrue(pageTitle.contains(expPageTitleLow));
	}
	
	public boolean pageTitleContains(String expPageTitle) throws Exception {
		System.out.println("Try to assert page title: " + expPageTitle);
		int i = 0;
		while (i < 10) {
			try {
				System.out.println(">>>Compare to page title: " + driver.getTitle());
				boolean b = driver.getTitle().contains(expPageTitle);
				return b;
			}
			catch (Exception e) {
				System.out.println("pageTitleContains exception: " + e);
				i = i + 1;
				Thread.sleep(50);
			}
		}
		return false;
	}
	
	public boolean pageUrlContains(String articleId) throws Exception {
		System.out.println("Try to assert page url: " + articleId);
		int i = 0;
		while (i < 10) {
			try {
				System.out.println(">>>Compare to page url: ");   // TODO: concatenate articleId
				boolean b = driver.getTitle().contains(articleId);
				return b;
			}
			catch (Exception e) {
				System.out.println("pageTitleContains exception: " + e);
				i = i + 1;
				Thread.sleep(50);
			}
		}
		return false;
	}

    public void assertPageContains(By locator, String expText) throws Exception {
        System.out.println("Try to assert page contains: " +  expText);

        int i = 0;
        while (i < 10) {
            try {
                driverFluentWait(1).until(ExpectedConditions.textToBePresentInElement(locator, expText));
                driver.findElement(locator).getText().contains(expText);
                break;
            }
            catch (Exception e) {
                System.out.println("----- assertPageContains Exception: " + e);
                i = i + 1;
                Thread.sleep(50);
            }
        }
        assertTrue(driver.findElement(locator).getText().contains(expText));
    }

    public void spinnerClickBy(By path) throws Exception {
        System.out.println("By: " + path);
        WebElement menuItem;
        int i = 0;
        while (i < 10) {
            try {
                menuItem = driver.findElement(path);
                menuItem.getLocation();
                driverFluentWait(1).until(ExpectedConditions.visibilityOf(menuItem));
                menuItem.getLocation();
                menuItem.click();
                break;
            }
            catch (Exception e) {
                System.out.println("spinnerClickBy exception: " + e);
                i = i + 1;
                Thread.sleep(50);
            }
        }
    }

    public void navigateStartUrl() {
        String bUrl = TestParams.getBaseUrl();
        driver.get(bUrl);
        //driverFluentWait(10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//footer")));
    }
}

