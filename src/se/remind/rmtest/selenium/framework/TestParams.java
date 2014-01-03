package se.remind.rmtest.selenium.framework;

/**
 * Created with IntelliJ IDEA.
 * User: oskeke
 * Date: 2013-09-24
 * Time: 12:19
 * To change this template use File | Settings | File Templates.
 */
public class TestParams {
    private static String testBaseUrl;

    public static void setBaseUrl(String baseUrl) {
        testBaseUrl = baseUrl;
    }

    public static String getBaseUrl() {
        String baseUrl;
        if (testBaseUrl == null) {
            baseUrl = "http://www.aftonbladet.se/";
        }
        else    {
            baseUrl = testBaseUrl;
        }
        return baseUrl;
    }
}
