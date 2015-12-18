package se.redmind.rmtest.cucumber.parameterized;

import org.junit.Assert;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Then;
import se.redmind.rmtest.runners.ParameterizedCucumber;

/**
 * @author Jeremy Comte
 */
@RunWith(ParameterizedCucumber.class)
@CucumberOptions(plugin = {"pretty", "json:target/ParameterizedStepsTest-json-report.json"})
public class ParameterizedStepsTest {

    public static class Steps {

        private int count;

        @Then("^this number is (\\d+)$")
        public void this_number_is(int count) {
            Assert.assertEquals(count, this.count);
        }

        @Then("^we write down the amount of letters in \"([^\"]*)\"$")
        public void we_write_down_the_amount_of_letters_in_value(String value) {
            count = value.length();
        }

        @Then("^I multiply it by (\\d+)$")
        public void i_multiply_it_by(int factor) {
            count *= factor;
        }

    }

}
