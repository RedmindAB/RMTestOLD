package se.aftonbladet.abtest.navigation.fotboll;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import com.google.common.base.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.redmind.rmtest.selenium.framework.AnApp;
import se.redmind.rmtest.selenium.framework.HTMLPage;

import java.util.List;
import java.util.concurrent.TimeUnit;



/**
 * @author oskeke
 */
public class FotbollNav extends AnApp {

	private By mTopBtnLink = By.id("android:id/up");
	private By mLeftMenuBox = By.id("se.aftonbladet.sportbladet.fotboll:id/leftDrawer");
	private String mRightMenuBox = "se.aftonbladet.sportbladet.fotboll:id/rightDrawer";
	private int mLongTimeout = 30;
	private int mShortTimeout = 10;
	private int mVeryShortTimeout = 1;

	/**
	 * @param pDriver
	 */
	public FotbollNav(WebDriver pDriver) throws Exception {
		super(pDriver);

	}

	public void initialStartNoAction() {
		driverWaitElementPresent(By.tagName("Button"), mShortTimeout);

		WebElement el = driver.findElement(By.tagName("Button"));
//		el.click();
		humanClick(el);
		driverWaitClickable(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"),mShortTimeout);
//		driverWaitClickable(By.xpath("//*[@id='nextButton']"),mShortTimeout);
		
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"));
//		el.click();
		humanClick(el);
		driverWaitClickable(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"),mShortTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"));
//		el.click();
		humanClick(el);
		driverWaitClickable(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"),mShortTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"));
//		el.click();
		humanClick(el);

		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper")),mShortTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper"));
//		el.click();
		humanClick(el);
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper")),mShortTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper"));
		humanClick(el);
//		el.click();

		//Condition for app to be loaded, might need more?
		if (driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(mLeftMenuBox), mVeryShortTimeout)) {
			WebElement topLeftBtn;
			topLeftBtn = driver.findElement(mTopBtnLink);
			humanClick(topLeftBtn);
		}
		
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/tournamentListView")), mShortTimeout);

	}


	public void openMenu()  {
//		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(mTopBtnLink), mShortTimeout);
		driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable(mTopBtnLink), mShortTimeout);
		WebElement topLeftBtn;
		topLeftBtn = driver.findElement(mTopBtnLink);
		
		humanClick(topLeftBtn);
		
		driverFluentWaitForCondition(ExpectedConditions.elementToBeClickable(mLeftMenuBox), mShortTimeout);
		//		try {
//			spinnerClickBy(mTopBtnLink);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	public void closeMenu()  {
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(mTopBtnLink), mShortTimeout);
		WebElement topLeftBtn;
		topLeftBtn = driver.findElement(mTopBtnLink);
		humanClick(topLeftBtn);
//		topLeftBtn.click();
	}
	
	
	public void humanClick(WebElement clickableElement){
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



	public void clickLeftSubMenuItem(String pMenuText, String pSubMenuText) throws Exception {
		System.out.println("clickLeftSubMenuItem pMenuText: " + pMenuText + ", and pSubMenuText: " + pSubMenuText);
		openMenu();
		clickMenuItemMore(pMenuText);
		clickSubMenuItem(pMenuText, pSubMenuText);
		//waitUntilDomReady();
		//driverFluentWait(30).until(ExpectedConditions.presenceOfElementLocated(mTopBtnLink));
	}

	public void clickLeftSubMenuItemExternal(String pMenuText, String pSubMenuText) throws Exception {
		System.out.println("clickLeftSubMenuItemExternal pMenuText: " + pMenuText + ", and pSubMenuText: " + pSubMenuText);
		openMenu();
		clickMenuItemMore(pMenuText);
		clickSubMenuItem(pMenuText, pSubMenuText);
		//waitUntilDomReady();
	}

	public void clickFirstTeaser() throws Exception {
		driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
		By teaserPath = By.xpath("//*[@id='abBody']/article[1]/a");
		//driverWaitClickable(By.xpath(teaserPath), 20);
		spinnerClickBy(teaserPath);
		driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(mTopBtnLink));
	}

	public void clickTeaserByArticleId(String articleId) throws Exception {
		driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
		By teaserPath = By.xpath("//*[@id='abBody']/article/a[contains(@href, '" + articleId + ".ab')]");
		//driverWaitClickable(By.xpath(teaserPath), 21);
		spinnerClickBy(teaserPath);
		driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
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
	
	private void clickMenuItemMore(String menuItemInnerText) throws Exception {
		By tMenuItemPath = By.xpath("//*[@id='abTopMenuDynamic']/li/a[text()='" + menuItemInnerText + "']/../a[text()='Mer' and not(@disabled)]");     // and not(@disabled)
		driverWaitClickable(tMenuItemPath, 6);
		spinnerClickBy(tMenuItemPath);
	}

	private void clickSubMenuItem(String menuItemInnerText, String subMenuText) throws Exception {
		By tMenuItemPath = By.xpath("//*[@id='abTopMenuDynamic']/li/a[text()='" + menuItemInnerText + "']/../ul[@class='abMenuSub']/li/a[text()='" + subMenuText + "' and not(@disabled)]"); // and not(@disabled)
		driverWaitClickable(tMenuItemPath, 6);
		spinnerClickBy(tMenuItemPath);
	}

	private String getHrefForName(String name, List<WebElement> elements) {
		for(int i = 0;i < elements.size();i++) {
			if(name.equalsIgnoreCase(elements.get(i).getText())) {
				return elements.get(i).getAttribute("href");
			}
		}
		return "";
	}




	public void clickTopBarItem(String pMenuText) throws Exception {
		By tMenuItemPath = By.xpath("//*[@id='abTopBar']/nav/ul/li/a[text()='" + pMenuText + "' and not(@disabled)]");
		driverWaitClickable(tMenuItemPath, 25);
		spinnerClickBy(tMenuItemPath);

		//waitUntilDomReady();
		//driverFluentWait(6).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
		//driverWait(6).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
	}

	public void clickTopBarSubItem(String pMenuText) throws Exception {
		By tMenuItemPath = By.xpath("//*[@id='abTopBar']/nav[@class='abSubNav']/ul/li/a[text()='" + pMenuText + "' and not(@disabled)]");
		driverWaitClickable(tMenuItemPath, 6);
		spinnerClickBy(tMenuItemPath);

		//waitUntilDomReady();
		//driverFluentWait(6).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
		//driverWait(6).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
	}

	public void navigateBack() throws Exception {
		int i = 0;
		while (i < 10) {
			try {
				driver.navigate().back();
				break;
			}
			catch (Exception e) {
				System.out.println("navigateBack exception: " + e);
				i = i + 1;
				Thread.sleep(50);
			}
		}
		//waitUntilDomReady();
	}

	public void assertPageContainsElementByClassName(String elementClassName) throws Exception {
		System.out.println("assertPageContainsElementByClassName: " + elementClassName);

		int i = 0;
		while (i < 10) {
			try {
				driverFluentWait(1).until(ExpectedConditions.presenceOfElementLocated(By.className(elementClassName)));
				break;
			}
			catch (Exception e) {
				System.out.println("----- assertPageContains Exception: " + e);
				i = i + 1;
				Thread.sleep(50);
			}
		}
		assertTrue(driver.findElement(By.className(elementClassName)).isDisplayed());

	}

	public void assertNumberOfElementsByXpath(String elementPath, int expNrElements) throws Exception {
		System.out.println("assertNumberOfElementsByXpath, xpath: " + elementPath + " Nr of elements: " + expNrElements);

		int elementSize = 0;
		int i = 0;
		while (i < 10) {
			try {
				driverFluentWait(1).until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementPath)));
				elementSize = driver.findElements(By.xpath(elementPath)).size();
				break;
			}
			catch (Exception e) {
				System.out.println("----- assertNumberOfElementsByXpath Exception: " + e);
				i = i + 1;
				Thread.sleep(50);
			}
		}
		assertEquals(expNrElements, elementSize);
	}
}

