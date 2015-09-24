package se.redmind.rmtest.appiumtest.systembolaget;

import static org.junit.Assert.assertEquals;

import io.appium.java_client.android.AndroidDriver;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;

/**
 * This is a AppiumChrome driver example, to run this example run startHub.sh and then startAppiumAndroid.sh like this.
 * To start a AppiumAndroid apk you need to run startAppiumAndroid.sh with the arguments /PATH/TO/APK "MAIN ACTIVITY" "WAIT ACTIVITY"
 * ./startAppiumAndroid /PATH/TO/APK ".debug.DebugMainActivity" ".activities.Splash"
 *
 * @author gustavholfve
 */
@RunWith(Parallelized.class)
public class AppiumTest {

    private AndroidDriver tDriver;
    private final DriverNamingWrapper urlContainer;
    private final String driverDescription;

    public AppiumTest(final DriverNamingWrapper driverWrapper,
                      final String driverDescription) {
        this.urlContainer = driverWrapper;
        this.driverDescription = driverDescription;
    }

    private static Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.ANDROID);
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
        Object[] wrapperList = getDrivers();
        for(int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i],
                    wrapperList[i].toString()});
        }
        return returnList;
    }

    @AfterClass
    public static void afterTest() {
        DriverProvider.stopDrivers();
    }

    @Before
    public void beforeTest() {
        this.tDriver = (AndroidDriver) this.urlContainer.startDriver();
    }

    private WebElement getInfoHeader() {
        WebElement infoHeader = tDriver
                .findElementById("se.systembolaget.android:id/activity_guide_info_header");
        return infoHeader;
    }

    @Before
    public void before() throws InterruptedException {
        String header = getTaskBarTitle();
        if(header.equals("Välkommen!")) {
            swipeRight();
            swipeRight();
            swipeRight();
            swipeRight();
            tDriver.findElementById("se.systembolaget.android:id/taskbarGuideClose").click();
        }
    }

    private String getTaskBarTitle() throws InterruptedException {
        String header = null;
        header = tDriver.findElementById("se.systembolaget.android:id/taskbarTitle").getText();
        return header;
    }

    @After
    public void after() {
        tDriver.findElementById("se.systembolaget.android:id/taskbarLogo").click();
    }

    private WebElement findButtonByText(String text) {
        List<WebElement> findElementsByClassName = tDriver.findElementsByClassName("android.widget.TextView");
        for(WebElement webElement : findElementsByClassName) {
            String buttonText = webElement.getText();
            if(buttonText.equals(text)) {
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

    private void getHeight() {
        int height = tDriver.manage().window().getSize().height;
    }

    private int getWidth() {
        int width = tDriver.manage().window().getSize().width;
        return width;
    }

    public void swipeRight() throws InterruptedException {
        Thread.sleep(500);
        tDriver.swipe(getWidth() - 50, 500, 10, 500, 200);
    }
}