package se.redmind.rmtest.selenium.example;

import com.google.common.base.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.redmind.rmtest.selenium.framework.HTMLPage;

import java.util.List;

import static org.junit.Assert.*;


public class CreateLogsNav extends HTMLPage {
	private WebDriver driver = getDriver();


	/**
	 * @param pDriver
	 */
	public CreateLogsNav(WebDriver pDriver) throws Exception {
		super(pDriver);
		driver = pDriver;

		int i = 0;
		while (i < 10) {
			try {
				driver.get("http://sl.se");
				break;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
