package se.aftonbladet.abtest.navigation.VK;

/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-22
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public class VKParams {
    private static String testBaseUrl;

    public static void setBaseUrl(String baseUrl) {
        testBaseUrl = baseUrl;
    }

    public static String getBaseUrl() {
        String baseUrl;
        if (testBaseUrl == null) {
            baseUrl = "http://stage.viktklubb.aftonbladet.se/v4";
        }
        else    {
            baseUrl = testBaseUrl;
        }
        return baseUrl;
    }
}
