package se.redmind.rmtest.selenium.example.testdroid;

import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GetDeviceTest  extends BaseTest {

    private static final String TARGET_APP_PATH = "/Users/petter/rmauto/AppTest/halebop_v2.1.4.32_20151013_release_dev_hockeyapp.apk";
    private static final String TESTDROID_SERVER = "http://appium.testdroid.com";
    private static int counter;

    @BeforeClass
    public static void setUp() throws Exception {
        getAFreeDevice();
    }
    @AfterClass
    public static void tearDown()
    {
        if (wd != null) {
            wd.quit();
        }
    }


    @Test
    public void mainPageTest() throws IOException, InterruptedException {

//        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//        takeScreenshot("start");
//        wd.findElement(By.xpath("//android.widget.RadioButton[@text='Use Testdroid Cloud']")).click();
//        wd.findElement(By.xpath("//android.widget.EditText[@resource-id='com.bitbar.testdroid:id/editText1']")).sendKeys("John Doe");
//        Assert.fail();
//        takeScreenshot("after_adding_name");
//        wd.navigate().back();
//        wd.findElement(By.xpath("//android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]")).click();
//        takeScreenshot("after_answer");
    }
}