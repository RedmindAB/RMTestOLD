package se.redmind.rmtest.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

/**
 * @author Jeremy Comte
 */
@RunWith(ParameterizedCucumber.class)
@CucumberOptions(glue = "se.redmind.rmtest.cucumber.web", plugin = {"pretty", "html:target/GoogleExample-html-report", "json:target/GoogleExample-json-report.json"})
public class GoogleExample {

}
