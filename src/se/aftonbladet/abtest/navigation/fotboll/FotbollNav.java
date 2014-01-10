package se.aftonbladet.abtest.navigation.fotboll;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	private int mLongTimeout = 30;
	private int mShortTimeout = 10;

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

		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper")),mLongTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper"));
//		el.click();
		humanClick(el);
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper")),mLongTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper"));
		humanClick(el);
//		el.click();

		//Condition for app to be loaded, might need more?
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/tournamentListView")), mLongTimeout);

	}


	public void openMenu()  {
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(mTopBtnLink), mShortTimeout);
		WebElement topLeftBtn;
		topLeftBtn = driver.findElement(mTopBtnLink);
		humanClick(topLeftBtn);
//		topLeftBtn.click();
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/leftDrawer")), mShortTimeout);

	}
	
	public void closeMenu()  {
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(mTopBtnLink), mShortTimeout);
		WebElement topLeftBtn;
		topLeftBtn = driver.findElement(mTopBtnLink);
		humanClick(topLeftBtn);
//		topLeftBtn.click();
	}
	
	public void humanClick(WebElement clickableElement){
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clickableElement.click();
	}

	public void clickLeftMenuItem(String pMenuText) throws Exception {
		System.out.println("clickLeftMenuItem pMenuText: " + pMenuText);
		openMenu();
		clickMenuItem(pMenuText);
		//waitUntilDomReady();
		driverFluentWait(31).until(ExpectedConditions.presenceOfElementLocated(mTopBtnLink));
	}

	public void clickLeftMenuItemExternal(String pMenuText) throws Exception {
		System.out.println("clickLeftMenuItemExternal pMenuText: " + pMenuText);
		openMenu();
		clickMenuItem(pMenuText);
		//waitUntilDomReady();
		//driverFluentWait(6).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));
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

	private void clickMenuItem(String menuItemInnerText) throws Exception {
		//System.out.println("menuItemInnerText: " + menuItemInnerText);
		By tMenuItemPath = By.xpath("//*[@id='abTopMenuDynamic']/li/a[text()='" + menuItemInnerText + "' and not(@disabled)]");
		//		driverWaitClickable(By.xpath(tMenuItemPath), 8);
		spinnerClickBy(tMenuItemPath);
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

