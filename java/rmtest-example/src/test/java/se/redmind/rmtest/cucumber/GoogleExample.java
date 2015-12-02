package se.redmind.rmtest.cucumber;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cucumber.api.CucumberOptions;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

/**
 * @author Jeremy Comte
 */
@RunWith(Parallelized.class)
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(glue = "se.redmind.rmtest.cucumber.web",
                 plugin = {"pretty", "html:target/GoogleExample-html-report", "json:target/GoogleExample-json-report.json"})
public class GoogleExample {

    @Parameterized.Parameters
    public static Collection<Object[]> drivers() {
        return DriverProvider.getDriversAsParameters();
    }

}
