package se.aftonbladet.abtest.navigation.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.redmind.rmtest.selenium.framework.AnApp;

public class SportsApps extends AnApp {

	private By mTopBtnLink = By.id("android:id/up");
	private By mLeftMenuBox = By.id("ShoudlBeOverridden"); 
	private By mRightMenuBox = By.id("ShoudlBeOverridden"); 

	public int mLongTimeout = 30;
	public int mShortTimeout = 10;
	public int mVeryShortTimeout = 1;

	public SportsApps(WebDriver pDriver) {
		super(pDriver);
		// TODO Auto-generated constructor stub
	}

	
	public By getmLeftMenuBox() {
		return mLeftMenuBox;
	}

	public By getmRightMenuBox() {
		return mRightMenuBox;
	}
	
	public By getmTopBtnLink() {
		return mTopBtnLink;
	}

	public void openMenu() {
			driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable(mTopBtnLink), mShortTimeout);
			WebElement topLeftBtn;
			topLeftBtn = driver.findElement(mTopBtnLink);
			
			humanClick(topLeftBtn);
			
			assertTrue("Menu Never Opened", driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable(getmLeftMenuBox()), mShortTimeout));
				
			
	
		}

	public void closeMenu() {
			driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(mTopBtnLink), mShortTimeout);
			WebElement topLeftBtn;
			topLeftBtn = driver.findElement(mTopBtnLink);
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
			driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable(clickableElement), mShortTimeout);
			clickableElement.click();
		
		}

	public void clickLeftMenuItem(String pMenuText) throws Exception {
		System.out.println("clickLeftMenuItem pMenuText: " + pMenuText);
		openMenu();
		WebElement menuButton = getMenuItemByText(pMenuText, By.id("se.aftonbladet.sportbladet.fotboll:id/menuButton"));
		
		humanClick(menuButton);
	
		//waitUntilDomReady();
		driverFluentWaitForCondition(ExpectedConditions.presenceOfElementLocated(mTopBtnLink), mShortTimeout);
	}

	private WebElement getMenuItemByText(String pMenuText, By pButtonId) {
			List<WebElement> menuButtons = driver.findElements(pButtonId);
			WebElement buttonElement = null;
			for (int i = 0; i < menuButtons.size(); i++) {
				if (menuButtons.get(i).getText().equalsIgnoreCase(pMenuText)) {
					buttonElement = menuButtons.get(i);
	//				humanClick(menuButtons.get(i));
					System.out.println("Hittat Knappen med text: " + pMenuText);
				}
			}
			assertFalse("Button could not be found: " + pMenuText, buttonElement == null);
			 return buttonElement;
		}


	
}
