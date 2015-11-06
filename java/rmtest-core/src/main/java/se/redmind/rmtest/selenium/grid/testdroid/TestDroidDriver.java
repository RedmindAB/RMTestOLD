package se.redmind.rmtest.selenium.grid.testdroid;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Key;
import com.testdroid.api.http.MultipartFormDataContent;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;

public class TestDroidDriver {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    // private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    // protected static AppiumDriver wd;
    private static int counter;
    private static DriverNamingWrapper dnw;

    public static DriverNamingWrapper getTestDroidWrapper() {
        final String TARGET_APP_PATH = "/Users/petter/rmauto/AppTest/halebop_v2.1.4.32_20151013_release_dev_hockeyapp.apk";
        final String TESTDROID_SERVER = "http://appium.testdroid.com";
        int counter;
        String testdroid_username = "petter.osterling@redmind.se";
        String testdroid_password = "Redmind1";

        if (StringUtils.isEmpty(testdroid_password) || StringUtils.isEmpty(testdroid_username)) {
            throw new IllegalArgumentException(
                    "Missing TESTDROID_USERNAME or/and TESTDROID_PASSWORD environment variables");
        }

        String fileUUID = null;
//        try {
//            fileUUID = uploadFile(TARGET_APP_PATH, TESTDROID_SERVER, testdroid_username, testdroid_password);
        fileUUID = "44c6526e-2746-45df-aca5-99f101ff88c7/halebop_v2.1.4.32_20151013_release_dev_hockeyapp.apk";
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("testdroid_target", "Android");
        capabilities.setCapability("deviceName", "Android Device");

        capabilities.setCapability("testdroid_username", testdroid_username);
        capabilities.setCapability("testdroid_password", testdroid_password);

        capabilities.setCapability("testdroid_project", "LocalAppium");
        capabilities.setCapability("testdroid_testrun", "Android Run 1");

        // See available devices at: https://cloud.testdroid.com/#public/devices
        capabilities.setCapability("testdroid_device", "NVIDIA SHIELD Tablet"); // Freemium
                                                                                // device
        capabilities.setCapability("testdroid_app", fileUUID); // to use
                                                               // existing app
                                                               // using "latest"
                                                               // as fileUUID

        // Optional
        // capabilities.setCapability("testdroid_description", "Appium project
        // description");
        // capabilities.setCapability("platformVersion", "4.4.2");
        // capabilities.setCapability("app-activity",
        // ".BitbarSampleApplicationActivity");
        // capabilities.setCapability("app", "com.bitbar.testdroid");

        System.out.println("Capabilities:" + capabilities.toString());

        System.out.println("Creating Appium session, this may take couple minutes..");
        URL testdroidURL = null;
        try {
            testdroidURL = new URL(TESTDROID_SERVER + "/wd/hub");
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        dnw = new DriverNamingWrapper(testdroidURL, capabilities, "NVIDIA SHIELD Tablet");

        return dnw;
    }

    protected static String uploadFile(String targetAppPath, String serverURL, String testdroid_username,
            String testdroid_password) throws IOException {
        final HttpHeaders headers = new HttpHeaders().setBasicAuthentication(testdroid_username, testdroid_password);

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                // request.setParser(new JsonObjectParser(JSON_FACTORY));
                request.setHeaders(headers);
            }

        });
        MultipartFormDataContent multipartContent = new MultipartFormDataContent();
        FileContent fileContent = new FileContent("application/octet-stream", new File(targetAppPath));

        MultipartFormDataContent.Part filePart = new MultipartFormDataContent.Part("file", fileContent);
        multipartContent.addPart(filePart);

        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(serverURL + "/upload"), multipartContent);

        HttpResponse response = request.execute();
        System.out.println("response:" + response.parseAsString());

        AppiumResponse appiumResponse = request.execute().parseAs(AppiumResponse.class);
        System.out.println("File id:" + appiumResponse.uploadStatus.fileInfo.file);

        return appiumResponse.uploadStatus.fileInfo.file;

    }

    protected void takeScreenshot(String screenshotName) {
        counter = counter + 1;
        String fullFileName = System.getProperty("user.dir") + "/Screenshots/" + getScreenshotsCounter() + "_"
                + screenshotName + ".png";

        screenshot(fullFileName);
    }

    private File screenshot(String name) {
        System.out.println("Taking screenshot...");
        File scrFile = ((TakesScreenshot) dnw.getAppiumDriver()).getScreenshotAs(OutputType.FILE);

        try {

            File testScreenshot = new File(name);
            FileUtils.copyFile(scrFile, testScreenshot);
            System.out.println("Screenshot stored to " + testScreenshot.getAbsolutePath());

            return testScreenshot;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getScreenshotsCounter() {
        if (counter < 10) {
            return "0" + counter;
        } else {
            return String.valueOf(counter);
        }
    }

    public static class AppiumResponse {
        Integer status;
        @Key("sessionId")
        String sessionId;

        @Key("value")
        TestDroidDriver.UploadStatus uploadStatus;

    }

    public static class UploadedFile {
        @Key("file")
        String file;
    }

    public static class UploadStatus {
        @Key("message")
        String message;
        @Key("uploadCount")
        Integer uploadCount;
        @Key("expiresIn")
        Integer expiresIn;
        @Key("uploads")
        TestDroidDriver.UploadedFile fileInfo;
    }
}