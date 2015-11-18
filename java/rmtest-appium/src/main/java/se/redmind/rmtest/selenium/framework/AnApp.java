package se.redmind.rmtest.selenium.framework;

import io.appium.java_client.AppiumDriver;

public class AnApp extends HTMLPage {

    public AnApp(AppiumDriver<?> pDriver) {
        super(pDriver);
    }

    @Override
    public final AppiumDriver<?> getDriver() {
        return (AppiumDriver<?>) driver;
    }
}
