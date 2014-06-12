package se.aftonbladet.abtest.navigation.app.fotboll;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import se.aftonbladet.abtest.navigation.app.SportsApps;


/**
 * @author oskeke
 */
public class FotbollNav extends SportsApps {

	public By mLeftMenuBox = By.id("se.aftonbladet.sportbladet.fotboll:id/leftDrawer");

	/**
	 * @param pDriver
	 */
	public FotbollNav(WebDriver pDriver) throws Exception {
		super(pDriver);

	}

	@Override
	public By getmLeftMenuBox() {
		return mLeftMenuBox;
	}


	public void initialStartNoAction() {
		driverWaitElementPresent(By.tagName("Button"), mShortTimeout);

		WebElement el = driver.findElement(By.tagName("Button"));
//		el.click();
		humanClick(el);
		driverWaitClickable(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"), mShortTimeout);
//		driverWaitClickable(By.xpath("//*[@id='nextButton']"),mShortTimeout);

		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"));
//		el.click();
		humanClick(el);
		driverWaitClickable(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"), mShortTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"));
//		el.click();
		humanClick(el);
		driverWaitClickable(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"), mShortTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/nextButton"));
//		el.click();
		humanClick(el);

		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper")), mShortTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper"));
//		el.click();
		humanClick(el);
		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper")), mShortTimeout);
		el = driver.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/viewFlipper"));
		humanClick(el);
//		el.click();

		//Condition for app to be loaded, might need more?
		if (driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(mLeftMenuBox), mVeryShortTimeout)) {
			WebElement topLeftBtn;
			topLeftBtn = driver.findElement(getmTopBtnLink());
			humanClick(topLeftBtn);
		}

		driverFluentWaitForCondition(ExpectedConditions.visibilityOfElementLocated(By.id("se.aftonbladet.sportbladet.fotboll:id/tournamentListView")), mShortTimeout);

	}

}

