package se.redmind.rmtest.cucumber.parameterized;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Then;
import se.redmind.rmtest.WebDriverWrapper;
import se.redmind.rmtest.runners.ParameterizedCucumberRunnerFactory;
import se.redmind.rmtest.runners.WebDriverRunner;

/**
 * @author Jeremy Comte
 */
@RunWith(WebDriverRunner.class)
@Parameterized.UseParametersRunnerFactory(ParameterizedCucumberRunnerFactory.class)
@CucumberOptions(plugin = {"pretty", "json:target/ParameterizedStepsTest-json-report.json"})
public class ParameterizedStepsTest {

    public static class Steps {

        private final WebDriverWrapper<?> wrapper;
        private int count;

        public Steps(WebDriverWrapper<?> driverWrapper) {
            this.wrapper = driverWrapper;
        }

        @Then("^this number is (\\d+)$")
        public void this_number_is(int count) {
            Assert.assertEquals(count, this.count);
        }

        @Then("^we write down the amount of letters in \"([^\"]*)\"$")
        public void we_write_down_the_amount_of_letters_in_value(String value) {
            count = value.length();
        }

        @Then("^I do some other stuff$")
        public void i_do_some_other_stuff() {
        }

    }

}
