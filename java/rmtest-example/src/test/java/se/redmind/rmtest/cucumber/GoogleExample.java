package se.redmind.rmtest.cucumber;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.redmind.rmtest.runners.DriverRunner;
import se.redmind.rmtest.runners.Parallelize;

/**
 * @author Jeremy Comte
 */
@RunWith(DriverRunner.class)
@Parallelize
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(glue = "se.redmind.rmtest.cucumber.web", plugin = "gherkin.formatter.NonRepeatingFormatter")
public class GoogleExample {

}
