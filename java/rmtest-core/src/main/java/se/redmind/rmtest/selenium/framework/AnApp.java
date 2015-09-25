package se.redmind.rmtest.selenium.framework;

import io.appium.java_client.AppiumDriver;

public class AnApp extends HTMLPage {

    private final AppiumDriver appDriver;

    public AnApp(AppiumDriver pDriver) {
        super(pDriver);
        appDriver = pDriver;
    }

    @Override
    public final AppiumDriver getDriver() {
        return appDriver;
    }
}
