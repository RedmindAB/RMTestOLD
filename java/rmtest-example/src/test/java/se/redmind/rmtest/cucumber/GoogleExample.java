package se.redmind.rmtest.cucumber;

import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Strings;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.utils.Try;

/**
 * @author Jeremy Comte
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/GoogleExample-html-report", "json:target/GoogleExample-json-report.json"})
public class GoogleExample {

    public static class Steps {

        private final DriverWrapper<WebDriver> wrapper;
        private final HTMLPage page;

        public Steps() {
            wrapper = (DriverWrapper<WebDriver>) DriverProvider.getDrivers()[0];
            page = new HTMLPage(wrapper.getDriver());
        }

        @When("^we navigate to the url \"([^\"]*)\"$")
        public void we_navigate_to_the_url(String url) {
            page.getDriver().get(url);
        }

        @Then("^the title contains \"([^\"]*)\"$")
        public void the_title_contains(String expectedTitle) {
            String pageTitle = Try.toGet(() -> page.getTitle())
                .until(value -> !Strings.isNullOrEmpty(value))
                .delayRetriesBy(500)
                .nTimes(10);
            assertTrue(pageTitle.startsWith(expectedTitle));
        }

        @Given("^that the screen is maximized$")
        public void that_the_screen_is_maximized() {
            page.getDriver().manage().window().maximize();
        }

        @Then("^the element at (.*) is \"([^\"]*)\"$")
        public void that_the_element_at_id_li_a_is(String xpath, String expectedValue) {
            WebElement yourElement = page.getDriver().findElement(By.xpath(xpath));
            String realValue = Try.toGet(() -> yourElement.getText())
                .until(value -> !Strings.isNullOrEmpty(value))
                .delayRetriesBy(500)
                .nTimes(10);
            Assert.assertEquals(expectedValue, realValue);
        }

    }

}
