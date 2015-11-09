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
public class SampleAppiumTest  extends TestdroidBase {

    private static final String TARGET_APP_PATH = "/Users/petter/rmauto/AppTest/halebop_v2.1.4.32_20151013_release_dev_hockeyapp.apk";
    private static final String TESTDROID_SERVER = "http://appium.testdroid.com";
    private static int counter;

    @BeforeClass
    public static void setUp() throws Exception {
        counter = 0;
        Map<String, String> env = System.getenv();
        String testdroid_username = "petter.osterling@redmind.se";
        String testdroid_password = "Redmind1";

        if(StringUtils.isEmpty(testdroid_password) || StringUtils.isEmpty(testdroid_username)) {
            throw new IllegalArgumentException("Missing TESTDROID_USERNAME or/and TESTDROID_PASSWORD environment variables");
        }

        String fileUUID = uploadFile(TARGET_APP_PATH, TESTDROID_SERVER, testdroid_username, testdroid_password);
//        String fileUUID = TARGET_APP_PATH;
        
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("testdroid_target", "Android");
        capabilities.setCapability("deviceName", "Android Device");
        
        capabilities.setCapability("testdroid_username", testdroid_username);
        capabilities.setCapability("testdroid_password", testdroid_password);
        
        capabilities.setCapability("testdroid_project", "LocalAppium");
        capabilities.setCapability("testdroid_testrun", "Android Run 1");
        
        // See available devices at: https://cloud.testdroid.com/#public/devices
        capabilities.setCapability("testdroid_device", "LG G4"); // Freemium device
        capabilities.setCapability("testdroid_app", fileUUID); //to use existing app using "latest" as fileUUID

        // Optional
        //capabilities.setCapability("testdroid_description", "Appium project description");
        //capabilities.setCapability("platformVersion", "4.4.2");
        //capabilities.setCapability("app-activity", ".BitbarSampleApplicationActivity");
        //capabilities.setCapability("app", "com.bitbar.testdroid");
        
        System.out.println("Capabilities:" + capabilities.toString());

        System.out.println("Creating Appium session, this may take couple minutes..");
        wd = new AndroidDriver(new URL(TESTDROID_SERVER+"/wd/hub"), capabilities);
        System.out.println(wd.getContextHandles().toString());
//        System.out.println(wd.getCurrentUrl());
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

        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        takeScreenshot("start");
        wd.findElement(By.xpath("//android.widget.RadioButton[@text='Use Testdroid Cloud']")).click();
        wd.findElement(By.xpath("//android.widget.EditText[@resource-id='com.bitbar.testdroid:id/editText1']")).sendKeys("John Doe");
        Assert.fail();
        takeScreenshot("after_adding_name");
        wd.navigate().back();
        wd.findElement(By.xpath("//android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]")).click();
        takeScreenshot("after_answer");
    }
}