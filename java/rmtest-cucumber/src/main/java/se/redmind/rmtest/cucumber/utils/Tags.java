package se.redmind.rmtest.cucumber.utils;

import cucumber.runtime.model.CucumberTagStatement;

/**
 * @author Jeremy Comte
 */
public class Tags {

    public static final String PARAMETERIZED = "@parameterized";
    public static final String QUIET = "@quiet";
    public static final String FULL = "@full";
    public static final String IGNORE = "@ignore";

    public static void addIgnoreToSystemProperties() {
        String property = System.getProperty("cucumber.options");
        if (property == null) {
            property = "";
        }
        if (!property.contains("~" + IGNORE)) {
            property += " --tags ~" + IGNORE;
            System.setProperty("cucumber.options", property);
        }
    }

    public static void addParameterizedToSystemProperties() {
        String property = System.getProperty("cucumber.options");
        if (property != null && !property.contains(PARAMETERIZED)) {
            property += " --tags " + PARAMETERIZED;
            System.setProperty("cucumber.options", property);
        }
    }

    public static boolean isParameterized(CucumberTagStatement statement) {
        return has(statement, PARAMETERIZED);
    }

    public static boolean isQuiet(CucumberTagStatement statement) {
        return has(statement, QUIET);
    }

    public static boolean has(CucumberTagStatement statement, String tagName) {
        return statement.getGherkinModel().getTags().stream().anyMatch(tag -> tagName.equals(tag.getName()));
    }

}
