package se.redmind.rmtest.selenium.framework;

public class TestParams {

    private static String testBaseUrl;
    public static String rmDeviceStype = "desktop";

    public static void setBaseUrl(String baseUrl) {
        testBaseUrl = baseUrl;
    }

    public static String getBaseUrl() {
        String baseUrl;
        if (testBaseUrl == null) {
            baseUrl = "http://www.aftonbladet.se/";
        } else {
            baseUrl = testBaseUrl;
        }
        return baseUrl;
    }
}
