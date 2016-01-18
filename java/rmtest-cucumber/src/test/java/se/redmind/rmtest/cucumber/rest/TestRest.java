package se.redmind.rmtest.cucumber.rest;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(glue="se.redmind.rmtest.cucumber.rest", plugin="pretty")
public class TestRest {
	
	private static volatile boolean isServerStarted = false;
	
	@BeforeClass
	public static void beforeClass() throws InterruptedException{
		new Thread(new Runnable() {
			@Override
			public void run() {
				new SparkServer().initServices();
			}
		}).start();
		Thread.sleep(1000);
	}
	
	public static class Steps{}
	
	
	
}
