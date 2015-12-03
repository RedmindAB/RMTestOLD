package se.redmind.rmtest.cucumber;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cucumber.api.CucumberOptions;
import se.redmind.rmtest.runners.Parallelize;
import se.redmind.rmtest.runners.RmTestRunner;

/**
 * @author Jeremy Comte
 */
@RunWith(RmTestRunner.class)
@Parallelize
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(glue = "se.redmind.rmtest.cucumber.web", plugin = "pretty")
public class GoogleExample {

}
