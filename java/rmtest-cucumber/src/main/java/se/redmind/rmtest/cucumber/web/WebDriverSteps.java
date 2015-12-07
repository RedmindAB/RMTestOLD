package se.redmind.rmtest.cucumber.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.utils.Try;

/**
 * @author Jeremy Comte
 */
public class WebDriverSteps {

    private static final String THAT = "(?:that )?";
    private static final String THE_USER = "(?:.*)?";
    private static final String THE_ELEMENT = "(?:the ?(?:button|element|field)?)?";
    private static final String IDENTIFIED_BY = "(?:with )?(named|id|xpath|class|css|(?:partial )?link|tag)? \"([^\"]*)\"";
    private static final String THE_ELEMENT_IDENTIFIED_BY = THE_ELEMENT + " ?" + IDENTIFIED_BY;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, By> aliases = new LinkedHashMap<>();
    private final DriverWrapper<WebDriver> driverWrapper;
    private final HTMLPage page;
    private WebElement currentElement;

    public WebDriverSteps(DriverWrapper<WebDriver> driverWrapper) {
        this.driverWrapper = driverWrapper;
        page = new HTMLPage(driverWrapper.getDriver());
    }

    // Helpers
    @When("^" + THAT + THE_USER + " know(?:s)? " + THE_ELEMENT_IDENTIFIED_BY + " as \"([^\"]*)\"$")
    public void that_we_know_the_element_named_as(String type, String id, String alias) {
        aliases.put(alias, by(type, id));
    }

    // Actions
    @When("^" + THAT + THE_USER + " navigate to the url \"([^\"]*)\"$")
    public void we_navigate_to_the_url(String url) {
        page.getDriver().get(url);
    }

    @When("^" + THAT + "the window is maximized$")
    public void the_window_is_maximized() {
        page.getDriver().manage().window().maximize();
    }

    @When("^" + THAT + THE_USER + " input(?:s)? \"([^\"]*)\" in " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void we_input_in_the_field_identified_by(String content, String type, String id) {
        find(by(type, id));
        currentElement.sendKeys(content);
    }

    @When("^" + THAT + THE_USER + " (?:press|enter)(?:[es])? (.*)$")
    public void that_we_press(Keys keys) {
        currentElement.sendKeys(keys);
    }

    @When("^" + THAT + THE_USER + " click(?:s)? on " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void that_we_click_on_the_button_identified_by(String type, String id) {
        find(by(type, id));
        currentElement.click();
    }

    @Then("^" + THAT + THE_USER + " wait(?:s)? (\\d+) (\\w+)")
    public void we_wait(int amount, TimeUnit timeUnit) throws InterruptedException {
        timeUnit.sleep(amount);
    }

    // Assertions
    @Then("^the title contains \"([^\"]*)\"$")
    public void the_title_contains(String expectedTitle) {
        String pageTitle = Try.toGet(() -> page.getTitle())
            .until(value -> !Strings.isNullOrEmpty(value))
            .delayRetriesBy(500)
            .nTimes(10);
        Assert.assertTrue(pageTitle.startsWith(expectedTitle));
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " is \"([^\"]*)\"$")
    public void that_the_element_at_id_is(String type, String id, String expectedValue) {
        find(by(type, id));
        String realValue = Try.toGet(() -> currentElement.getText())
            .until(value -> !Strings.isNullOrEmpty(value))
            .delayRetriesBy(500)
            .nTimes(10);
        Assert.assertEquals(expectedValue, realValue);
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " is present$")
    public void the_element_with_id_is_present(String type, String id) {
        find(by(type, id));
    }

    private WebElement find(By elementLocation) {
        page.driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(elementLocation));
        currentElement = page.getDriver().findElement(elementLocation);
        return currentElement;
    }

    @After
    public void stopDriver() {
        driverWrapper.stopDriver();
    }

    public By by(String type, String value) {
        Preconditions.checkArgument(value != null);
        if (type == null) {
            return aliases.get(value);
        } else {
            switch (type) {
                case "named":
                    return By.name(value);
                case "id":
                    return By.id(value);
                case "xpath":
                    return By.xpath(value);
                case "class":
                    return By.className(value);
                case "css":
                    return By.cssSelector(value);
                case "link":
                    return By.linkText(value);
                case "partial link":
                    return By.partialLinkText(value);
                case "tag":
                    return By.tagName(value);
                default:
                    throw new IllegalArgumentException("unknowm parameter '" + type + "'");
            }
        }

    }

}
