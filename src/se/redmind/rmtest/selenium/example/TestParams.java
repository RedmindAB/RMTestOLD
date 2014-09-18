package se.redmind.rmtest.selenium.example;

public class TestParams {
    private static String testBaseUrl;

    public static void setBaseUrl(String baseUrl) {
        testBaseUrl = baseUrl;
    }

    public static String getBaseUrl() {
        String baseUrl;
        if (testBaseUrl == null) {
            baseUrl = "http://www.redmind.se/";
        }
        else    {
            baseUrl = testBaseUrl;
        }
        return baseUrl;
    }
}
