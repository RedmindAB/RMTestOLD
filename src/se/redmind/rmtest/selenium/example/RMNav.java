package se.redmind.rmtest.selenium.example;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.redmind.rmtest.selenium.framework.HTMLPage;

import java.util.List;

public class RMNav extends HTMLPage {
	private WebDriver driver = getDriver();
	private Actions builder;

	/**
	 * @param pDriver
	 */
	public RMNav(WebDriver pDriver, String serverUrl) throws Exception {
		super(pDriver);
		driver = pDriver;

		int i = 0;
		while (i < 10) {
			try {
				driver.get(Objects.firstNonNull(serverUrl,
						"http://www.redmind.se"));
				// driverFluentWait(6).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
				break;
			} catch (Exception e) {
				System.out.println(i + " AbMobileNav: " + e);
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
		System.out.println("Clicking: " + pMenuText);

		driver.findElement(By.cssSelector(getCssSelector(pMenuText))).click();

	}

	public void openMobileMenu() throws Exception {
		System.out.println("Opening menu");
		driver.findElement(By.className("mobile-menu-control")).click();
	}

	public void clickOnSubmenu(String pMenuText, String pSubMenuText)
			throws Exception {
		Actions builder = new Actions(driver);

		builder.moveToElement(
				driver.findElement(By.cssSelector(getCssSelector(pMenuText))))
				.perform();

		System.out.println("Hovering over " + pMenuText);
		
		driverFluentWait(20).until(ExpectedConditions.elementToBeClickable(By.className("sub-menu")));
		
		System.out.println("Pressing " + pSubMenuText);
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

		System.out.println("Hovering over " + pMenuText);

		driverFluentWait(20).until(ExpectedConditions.elementToBeClickable(By.className("sub-menu")));

		builder.moveToElement(
				driver.findElement(By.cssSelector(getCssSelector(pSubMenuText))))
				.perform();

		System.out.println("Hovering over " + pSubMenuText);
		
		driverFluentWait(20).until(ExpectedConditions.elementToBeClickable(By.className("sub-menu")));

		System.out.println("Pressing " + pSubSubMenuText);
		driver.findElement(By.cssSelector(getCssSelector(pSubSubMenuText)))
				.click();
		
		driverFluentWait(20);
	}
}