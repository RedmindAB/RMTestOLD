package se.redmind.rmtest.comaround.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.redmind.rmtest.selenium.framework.HTMLPage;

public class ComarounHeaderdNav extends HTMLPage {

	private WebDriver driver;
	private int globalWait = 4;

	public ComarounHeaderdNav(WebDriver pDriver) {
		super(pDriver);

		this.driver = pDriver;
		while (true) {
			try {
				this.driver.get("http://www.comaround.se");
				break;
			} catch (Exception e) {
			}
		}
	}

    @Override
	public String getTitle(){
		return driver.getTitle();
	}

	public String getCurrentURL(){
		return driver.getCurrentUrl();
	}

	public void clickComAroundZeroNav(){
		driverFluentWait(globalWait);
		String cssSelector = ".menu.main-menu>:nth-child(2)>a";
		clickByCssSelector(cssSelector);
		waitForTitleContaining("Zero");
	}

	public void clickKonceptet() {
		driverFluentWait(globalWait);
		String cssSelector = ".menu.main-menu>:nth-child(3)>a";
		clickByCssSelector(cssSelector);
		waitForTitleContaining("Konceptet");
	}

	public void clickInspiration() {
		driverFluentWait(globalWait);
		String cssSelector = ".menu.main-menu>:nth-child(4)>a";
		clickByCssSelector(cssSelector);
		waitForTitleContaining("Inspiration");
	}

	public void clickReferenser() {
		driverFluentWait(globalWait);
		String cssSelector = ".menu.main-menu>:nth-child(5)>a";
		clickByCssSelector(cssSelector);
		waitForTitleContaining("Referenser");
	}

	public void clickPrismodell() {
		driverFluentWait(globalWait);
		String cssSelector = ".menu.main-menu>:nth-child(6)>a";
		clickByCssSelector(cssSelector);
		waitForTitleContaining("Prismodell");
	}

	public void clickSkapaKonto() {
		driverFluentWait(globalWait);
		String cssSelector = ".menu.main-menu>:nth-child(7)>a";
		clickByCssSelector(cssSelector);
		waitForTitleContaining("Skapa");
	}

	private void clickByCssSelector(String cssPath){
		By locator = By.cssSelector(cssPath);
		driverWaitElementPresent(locator, 20);
		driverFluentWaitForCondition(ExpectedConditions.presenceOfElementLocated(locator), 20);
		driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable(locator), globalWait);
		driverWaitClickable(locator, globalWait);
		WebElement element = driver.findElement(locator);
		element.click();
	}

	private void waitForTitleContaining(String title){
		driverFluentWaitForCondition(ExpectedConditions.titleContains(title), 10);
	}

}
