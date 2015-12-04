package se.redmind.rmtest.appiumtest.systembolaget;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import se.redmind.rmtest.AppiumDriverWrapper;

import static org.junit.Assert.assertEquals;

import se.redmind.rmtest.config.Configuration;
import se.redmind.rmtest.runners.FilterDrivers;
import se.redmind.rmtest.runners.RmTestRunner;

import static org.junit.Assert.assertEquals;

/**
 * <pre>
 * This is a AppiumChrome driver example, to run this example run startHub.sh and then startAppiumAndroid.sh like this.
 * To start a AppiumAndroid apk you need to run startAppiumAndroid.sh with the arguments /PATH/TO/APK "MAIN ACTIVITY" "WAIT ACTIVITY"
 * ./startAppiumAndroid /PATH/TO/APK ".debug.DebugMainActivity" ".activities.Splash"
 * </pre>
 *
 * @author gustavholfve
 */
@RunWith(RmTestRunner.class)
@FilterDrivers(platforms = Platform.ANDROID)
public class AppiumTest {

    private AppiumDriver<WebElement> androidDriver;
    private final AppiumDriverWrapper driverWrapper;

    public AppiumTest(AppiumDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;
        this.androidDriver = this.driverWrapper.getDriver();

    }

    @AfterClass
    public static void afterTest() {
        Configuration.current().stopAllDrivers();
    }

    @Before
    public void before() throws InterruptedException {
        String header = getTaskBarTitle();
        if (header.equals("Välkommen!")) {
            swipeRight();
            swipeRight();
            swipeRight();
            swipeRight();
            androidDriver.findElementById("se.systembolaget.android:id/taskbarGuideClose").click();
        }
    }

    private String getTaskBarTitle() throws InterruptedException {
        return androidDriver.findElementById("se.systembolaget.android:id/taskbarTitle").getText();
    }

    @After
    public void after() {
        androidDriver.findElementById("se.systembolaget.android:id/taskbarLogo").click();
    }

    private WebElement findButtonByText(String text) {
        List<WebElement> findElementsByClassName = androidDriver.findElementsByClassName("android.widget.TextView");
        for (WebElement webElement : findElementsByClassName) {
            String buttonText = webElement.getText();
            if (buttonText.equals(text)) {
                return webElement;
            }
        }
        return null;
    }

    @Test
    public void clickDrycker() throws Exception {
        findButtonByText("Drycker").click();
        assertEquals("Drycker", getTaskBarTitle());
    }

    @Test
    public void clickButikermm() throws Exception {
        findButtonByText("Butiker & ombud").click();
        assertEquals("Butiker & ombud", getTaskBarTitle());
    }

    @Test
    public void clickSparat() throws Exception {
        findButtonByText("Sparat").click();
        assertEquals("Sparat", getTaskBarTitle());
    }

    @Test
    public void clickInkoepslista() throws Exception {
        findButtonByText("Inköpslista").click();
        assertEquals("Inköpslista", getTaskBarTitle());
    }

    @Test
    public void clickbraeaetvaejta() throws Exception {
        findButtonByText("Bra att veta").click();
        assertEquals("Bra att veta", getTaskBarTitle());
    }

    @Test
    public void clickKuendshejncht() throws Exception {
        findButtonByText("Kundtjänst").click();
        assertEquals("Kundtjänst", getTaskBarTitle());
    }

    private int getWidth() {
        return androidDriver.manage().window().getSize().width;
    }

    public void swipeRight() throws InterruptedException {
        Thread.sleep(500);
        androidDriver.swipe(getWidth() - 50, 500, 10, 500, 200);
    }

}
