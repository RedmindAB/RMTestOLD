package se.aftonbladet.abtest.navigation.app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import se.redmind.rmtest.selenium.framework.AnApp;

import static org.junit.Assert.assertTrue;


/**
 * Created with IntelliJ IDEA.
 * User: redben
 * Date: 10/04/14
 * Time: 14:15
 */
public class AbseApp extends AnApp {

	public int aLongTimeout = 30;
	public int aNormalTimeout = 20;
	public int aShortTimeout = 10;
	public int aVeryShortTimeout = 1;
	private By aTopBtnLink = By.id("se.aftonbladet.start:id/menuButton");
	private By aLeftMenuBox = By.id("se.aftonbladet.start:id/sideMenu");
	private By aRightMenuBox = By.id("se.aftonbladet.start:id/sideUserMenu");


	public AbseApp(WebDriver pDriver) {
		super(pDriver);

	}

	public By getaLeftMenuBox() {
		return aLeftMenuBox;
	}

	public By getaRightMenuBox() {
		return aRightMenuBox;
	}

	public By getaTopBtnLink() {
		return aTopBtnLink;
	}

	public void OpenMenu() {
		driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable(aTopBtnLink), aShortTimeout);
		WebElement topLeftBtn;
		topLeftBtn = driver.findElement(aTopBtnLink);

		humanClick(topLeftBtn);

		assertTrue("Menu Never Opened", driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable(getaLeftMenuBox()), aShortTimeout));


	}

	public void closeMenu() {
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(aTopBtnLink), aShortTimeout);
		WebElement topLeftBtn;
		topLeftBtn = driver.findElement(aTopBtnLink);
		humanClick(topLeftBtn);
		//		topLeftBtn.click();
	}
	public void humanClick(WebElement clickableElement) {
		//		try {
		//			TimeUnit.MILLISECONDS.sleep(100);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

		driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable((By) clickableElement), aShortTimeout);
		clickableElement.click();

	}


}