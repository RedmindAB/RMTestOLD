package se.aftonbladet.abtest.navigation.app.ABHybrid;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import se.aftonbladet.abtest.navigation.app.AbseApp;

/**
 * Created with IntelliJ IDEA.
 * User: redben
 * Date: 10/04/14
 * Time: 09:15
 */
public class AbseNav extends AbseApp {
	public By aleftMenuBox = By.id("se.aftonbladet.start:id/menuButton");


	public AbseNav(WebDriver pDriver) throws Exception {
		super(pDriver);
	}

	@Override
	public By getaLeftMenuBox () {
		return aleftMenuBox;
	}

}
