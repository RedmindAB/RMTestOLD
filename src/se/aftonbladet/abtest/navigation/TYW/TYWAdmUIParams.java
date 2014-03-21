package se.aftonbladet.abtest.navigation.TYW;

/**
 * Created with IntelliJ IDEA.
 * User: ninham1
 * Date: 2014-01-21
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
public class TYWAdmUIParams {
    private static String testBaseUrl;

    public static void setBaseUrl(String baseUrl) {
        testBaseUrl = baseUrl;
    }

    public static String getBaseUrl() {
        String baseUrl;
        if (testBaseUrl == null) {
            baseUrl = "http://www.stage3.abse.aftonbladet.se:8088";
        }
        else    {
            baseUrl = testBaseUrl;
        }
        return baseUrl;
    }
}
