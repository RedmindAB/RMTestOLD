package se.redmind.rmtest.cucumber.rest;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(glue = "se.redmind.rmtest.cucumber.rest", plugin = "pretty")
public class TestRest {

    private static volatile boolean isServerStarted = false;
    private static SparkServer sparkServer;
	private static int localPort;

    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        try {
			sparkServer = new SparkServer().initServices();
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
        localPort = sparkServer.getLocalPort();
    }

    public static class Steps {
    	
    	@Given("^custom port is the same as webserver;$")
    	public void custom_port_is_the_same_as_webserver() throws Throwable {
    		RestStep.PORT = localPort;
    	}
    	
    }

}
