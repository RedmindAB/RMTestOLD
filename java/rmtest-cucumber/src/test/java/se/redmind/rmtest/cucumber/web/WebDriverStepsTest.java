package se.redmind.rmtest.cucumber.web;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cucumber.api.CucumberOptions;
import se.redmind.rmtest.cucumber.CucumberParametersRunnerFactory;
import se.redmind.rmtest.runners.DriverRunner;

/**
 * @author Jeremy Comte
 */
@RunWith(DriverRunner.class)
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(plugin = "gherkin.formatter.NonRepeatingFormatter")
public class WebDriverStepsTest {

}
