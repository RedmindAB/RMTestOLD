package se.redmind.rmtest.cucumber.web;

import java.net.URLClassLoader;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.runners.Parameterized;

import cucumber.api.CucumberOptions;
import se.redmind.rmtest.cucumber.CucumberParametersRunnerFactory;

import static spark.Spark.*;

/**
 * @author Jeremy Comte
 */
//@RunWith(DriverRunner.class)
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(plugin = "gherkin.formatter.NonRepeatingFormatter")
public class WebDriverStepsTest {

    private static boolean isServerRunning;

    @BeforeClass
    public static synchronized void createTestServer() {
        if (!isServerRunning) {
            isServerRunning = true;
            System.out.println(Arrays.toString(((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs()));
            get("/", (request, response) -> "hello!");
            get("/cookie/valueOf/:name", (request, response) -> {
                response.type("text/plain");
                return String.valueOf(request.cookie(request.params("name")));
            });
        }
    }
}
