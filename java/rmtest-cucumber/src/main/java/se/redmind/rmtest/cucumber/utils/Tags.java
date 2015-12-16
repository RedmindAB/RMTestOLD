package se.redmind.rmtest.cucumber.utils;

/**
 * @author Jeremy Comte
 */
public class Tags {

    public static void addIgnoreToSystemProperties() {
        String property = System.getProperty("cucumber.options");
        if (property == null) {
            property = "";
        }
        if (!property.contains("~@ignore")) {
            property += " --tags ~@ignore";
            System.setProperty("cucumber.options", property);
        }
    }

    public static void addParameterizedToSystemProperties() {
        String property = System.getProperty("cucumber.options");
        if (property == null) {
            property = "";
        }
        if (!property.contains("~@parameterized")) {
            property += " --tags ~@parameterized";
            System.setProperty("cucumber.options", property);
        }
    }

}
