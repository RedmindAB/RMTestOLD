package se.redmind.rmtest.selenium.example.testdroid;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.testdroid.api.http.MultipartFormDataContent;

import io.appium.java_client.AppiumDriver;

public class TestdroidBase {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    protected static AppiumDriver wd;
    private static int counter;

    public static String uploadFile(String targetAppPath, String serverURL, String testdroid_username,
            String testdroid_password) throws IOException {
        final HttpHeaders headers = new HttpHeaders().setBasicAuthentication(testdroid_username, testdroid_password);

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
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

    protected static void getAFreeDevice() throws IOException {
        final HttpHeaders headers = new HttpHeaders().setAcceptEncoding("application/json");

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
                request.setHeaders(headers);
            }
        });
        // APIDevice.OsType.ANDROID;
        // APIDeviceQueryBuilder devQuery = new APIDeviceQueryBuilder();
        // devQuery.filterWithDeviceFilters(APIDevice.OsType.ANDROID)
        ////

        HttpRequest request = requestFactory
                .buildGetRequest(new GenericUrl("https://cloud.testdroid.com/api/v2/devices"));

        HttpResponse response = request.execute();
        System.out.println("response:" + response.parseAsString());
        //
        // AppiumResponse appiumResponse =
        // request.execute().parseAs(AppiumResponse.class);
        // System.out.println("File id:" +
        // appiumResponse.uploadStatus.fileInfo.file);

        // return appiumResponse.uploadStatus.fileInfo.file;

    }

    protected void takeScreenshot(String screenshotName) {
        counter = counter + 1;
        String fullFileName = System.getProperty("user.dir") + "/Screenshots/" + getScreenshotsCounter() + "_"
                + screenshotName + ".png";

        screenshot(fullFileName);
    }

    private File screenshot(String name) {
        System.out.println("Taking screenshot...");
        File scrFile = ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);

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
        TestdroidBase.UploadStatus uploadStatus;

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
        TestdroidBase.UploadedFile fileInfo;
    }
}