package se.redmind.rmtest.cucumber.utils;

import org.junit.Assert;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.junit.Cucumber;

/**
 * @author Jeremy Comte
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "json:target/TagsTest-json-report.json", "html:target/TagsTest-hmtl-report"})
public class TagsTest {

    public static class Steps {

        @Given("^that the cucumber options are \"([^\"]*)\"$")
        public void that_the_cucumber_options_are(String options) {
            System.setProperty("cucumber.options", options.trim());
            Tags.addParameterizedToSystemProperties();
            Tags.addIgnoreToSystemProperties();
        }

        @Then("^the overriden value is \"([^\"]*)\"$")
        public void the_overriden_value_is(String options) {
            Assert.assertEquals(options.trim(), System.getProperty("cucumber.options").trim());
        }
    }

}
