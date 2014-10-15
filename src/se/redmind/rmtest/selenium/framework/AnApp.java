package se.redmind.rmtest.selenium.framework;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.WebDriver;

public class AnApp extends HTMLPage{
	private WebDriver appDriver;
	public AnApp(WebDriver pDriver) {
		super(pDriver);
		appDriver = pDriver;
		// TODO Auto-generated constructor stub
	}
	
	 public final WebDriver getDriver() {
	        return appDriver;
	    }    
}
