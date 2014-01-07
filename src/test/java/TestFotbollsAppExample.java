package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.aftonbladet.abtest.navigation.AbMobileNav;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.framework.TestParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.rmtest.selenium.grid.SwipeableWebDriver;


@RunWith(Parallelized.class)
public class TestFotbollsAppExample {
    private WebDriver tDriver;
    private AbMobileNav tNavPage;
    private String startUrl = TestParams.getBaseUrl();

    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;
    
    public TestFotbollsAppExample(final DriverNamingWrapper driverWrapper, final String driverDescription) {
       this.driverWrapper = driverWrapper;
       this.driverDescription = driverDescription;
    }
    
    private static Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.MAC);
        
    }
    
    
    @Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
        Object[] wrapperList = getDrivers();
        for (int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
        }
        return returnList;
    }
    
    @Test
    public void testFotboll() throws Exception {
//        File classpathRoot = new File(System.getProperty("user.dir"));
//        File appDir = new File(classpathRoot, "apps");
//        File appFile = new File(appDir, "Sportbladet-Fotboll-2.1-SNAPSHOT.apk");
//        DesiredCapabilities capabilities = driverWrapper.getCapabilities();
//        capabilities.setCapability("device","Android");
////        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
////        capabilities.setCapability(CapabilityType.VERSION, "4.4");
//        capabilities.setCapability(CapabilityType.PLATFORM, "MAC");
//        capabilities.setCapability("app", appFile.getAbsolutePath());
//        capabilities.setCapability("app-package", "se.aftonbladet.sportbladet.fotboll");
//        capabilities.setCapability("app-activity", "MainActivity");
        
        WebDriver driver = driverWrapper.getDriver();
        HTMLPage app = new HTMLPage(driver);
        app.driverWaitElementPresent(By.tagName("Button"), 10);
        
        WebElement el = driver.findElement(By.tagName("Button"));
        el.click();
        app.driverWaitElementPresent(By.id("nextButton"),10);
        el = driver.findElement(By.id("nextButton"));
        el.click();
        app.driverWaitElementPresent(By.id("nextButton"),10);
        el = driver.findElement(By.id("nextButton"));
        el.click();
        app.driverWaitElementPresent(By.id("nextButton"),10);
        el = driver.findElement(By.id("nextButton"));
        el.click();
        app.driverWaitElementPresent(By.tagName("LinearLayout"),10);
        el = driver.findElement(By.id("nextButton"));
        el.click();
        app.driverWaitElementPresent(By.tagName("LinearLayout"),10);
        el = driver.findElement(By.id("nextButton"));
        el.click();
//        app = new HTMLPage(driver);app.driverWaitElementPresent(By.tagName("ViewFlipper"), 10);
        el = driver.findElement(By.tagName("ViewFlipper"));
        el.click();
        app.driverWaitElementPresent(By.id("action_bar_title"), 10);
        el = driver.findElement(By.name("action_bar_title"));
        assertEquals(el.getText(), "Matcher");
        el.click();
        app.driverWaitElementPresent(By.name("leftDrawer"), 10);
        el = driver.findElement(By.tagName("text"));
        assertEquals(el.getText(), "API Demos");
        el = driver.findElement(By.name("App"));
        el.click();
        List<WebElement> els = driver.findElements(By.tagName("text"));
        assertEquals(els.get(2).getText(), "Activity");
    }



}