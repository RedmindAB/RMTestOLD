package se.redmind.rmtest.cucumber;



import cucumber.api.CucumberOptions;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Given;
import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.runners.DriverRunner;
import se.redmind.rmtest.runners.Parallelize;
import se.redmind.rmtest.runners.ReuseDriverBetweenTests;

/**
 * @author Jeremy Comte
 */
@RunWith(DriverRunner.class)
@Parallelize()
@Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
@CucumberOptions(glue = {"se.redmind.rmtest.cucumber"}, plugin = "gherkin.formatter.NonRepeatingFormatter")
@ReuseDriverBetweenTests
public class GoogleExample {

    public static class Steps {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        private final DriverWrapper<?> driverWrapper;

        public Steps(DriverWrapper<?> driverWrapper) {
            this.driverWrapper = driverWrapper;
        }

        @Given("^that we send a rocket named \"([^\"]*)\" to the moon$")
        public void that_we_send_a_rocket_named_to_the_moon(String name) {
            logger.info("roger ... the " + name + " rocket has landed and we have a " + driverWrapper.getDriver());
        }
    }

}
