package se.redmind.rmtest.cucumber.web;

import org.junit.BeforeClass;
import org.junit.runners.Parameterized;

import cucumber.api.CucumberOptions;

import org.junit.runner.RunWith;

import se.redmind.rmtest.cucumber.CucumberParametersRunnerFactory;
import se.redmind.rmtest.runners.DriverRunner;
import se.redmind.rmtest.runners.ReuseDriverBetweenTests;

import static spark.Spark.*;

/**
 * @author Jeremy Comte
 */
@RunWith(DriverRunner.class)
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(plugin = "gherkin.formatter.NonRepeatingFormatter")
@ReuseDriverBetweenTests
public class WebDriverStepsTest {

    private static boolean isServerRunning;

    @BeforeClass
    public static synchronized void createTestServer() {
        if (!isServerRunning) {
            isServerRunning = true;
            get("/cookie/valueOf/:name", (request, response) -> {
                response.type("text/plain");
                return String.valueOf(request.cookie(request.params("name")));
            });
        }
    }

}
