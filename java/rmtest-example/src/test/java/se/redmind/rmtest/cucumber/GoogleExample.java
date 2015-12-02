package se.redmind.rmtest.cucumber;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cucumber.api.CucumberOptions;
import se.redmind.rmtest.selenium.grid.DriverProvider;

/**
 * @author Jeremy Comte
 */
@RunWith(ParameterizedCucumber.class)
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(glue = "se.redmind.rmtest.cucumber.web",
                 plugin = {"pretty", "html:target/GoogleExample-html-report", "json:target/GoogleExample-json-report.json"})
public class GoogleExample {

    @Parameterized.Parameters
    public static Collection<Object[]> drivers() {
        return Arrays.asList(DriverProvider.getDrivers()).stream().map(obj -> new Object[]{obj}).collect(Collectors.toList());
    }

}
