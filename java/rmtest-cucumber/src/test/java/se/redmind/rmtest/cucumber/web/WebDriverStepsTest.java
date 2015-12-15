package se.redmind.rmtest.cucumber.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import se.redmind.rmtest.WebDriverWrapper;
import se.redmind.rmtest.cucumber.CucumberParametersRunnerFactory;
import static se.redmind.rmtest.cucumber.web.WebDriverSteps.*;
import se.redmind.rmtest.runners.ReuseDriverBetweenTests;
import se.redmind.rmtest.runners.WebDriverRunner;
import se.redmind.utils.Fields;
import spark.Spark;
import static spark.Spark.*;
import spark.webserver.JettySparkServer;

/**
 * @author Jeremy Comte
 */
@RunWith(WebDriverRunner.class)
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(plugin = {"gherkin.formatter.NonRepeatingFormatter", "json:target/BaseConfigurationTest-json-report.json"})
@ReuseDriverBetweenTests
public class WebDriverStepsTest {

    private static boolean isServerRunning;
    private static int localPort;

    @BeforeClass
    public static synchronized void createTestServer() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (!isServerRunning) {
            isServerRunning = true;
            Spark.port(0);
            get("/cookie/valueOf/:name", (request, response) -> {
                response.type("text/plain");
                return String.valueOf(request.cookie(request.params("name")));
            });
            Spark.awaitInitialization();
            JettySparkServer sparkServer = Fields.getValue(Spark.getInstance(), "server");
            Server jettyServer = Fields.getValue(sparkServer, "server");
            ServerConnector connector = (ServerConnector) jettyServer.getConnectors()[0];
            localPort = connector.getLocalPort();
        }
    }

    public static class Steps {

        private final WebDriverWrapper<?> wrapper;

        public Steps(WebDriverWrapper<?> driverWrapper) {
            this.wrapper = driverWrapper;
        }

        @Given("^" + THAT + THE_USER + " navigate(?:s)? to our local spark at \"/([^\"]*)\"$")
        public void that_we_navigate_to_our_local_spark_at(String path) {
            wrapper.getDriver().navigate().to("http://localhost:" + localPort + "/" + path);
        }

    }

}
