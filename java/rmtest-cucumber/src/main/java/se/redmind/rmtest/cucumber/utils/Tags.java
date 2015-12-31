package se.redmind.rmtest.cucumber.utils;

import cucumber.runtime.model.CucumberTagStatement;

/**
 * @author Jeremy Comte
 */
public class Tags {

    public static final String PARAMETERIZED = "@parameterized";
    public static final String QUIET = "@quiet";
    public static final String IGNORE = "@ignore";

    public static void addIgnoreToSystemProperties() {
        String property = System.getProperty("cucumber.options");
        if (property == null) {
            property = "";
        }
        if (!property.contains("~" + IGNORE)) {
            property += " --tags ~@" + IGNORE;
            System.setProperty("cucumber.options", property);
        }
    }

    public static boolean isParameterized(CucumberTagStatement statement) {
        return is(statement, PARAMETERIZED);
    }

    public static boolean isQuiet(CucumberTagStatement statement) {
        return is(statement, QUIET);
    }

    public static boolean is(CucumberTagStatement statement, String tagName) {
        return statement.getGherkinModel().getTags().stream().anyMatch(tag -> tagName.equals(tag.getName()));
    }

}
