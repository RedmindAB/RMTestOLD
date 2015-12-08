package se.redmind.rmtest.cucumber.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.Assert;
import org.openqa.selenium.*;
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
    private static final String IDENTIFIED_BY = "(?:with )?(named|id|xpath|class|css|(?:partial )?link text|tag)? ?\"(.*)\"";
    private static final String THE_ELEMENT_IDENTIFIED_BY = THE_ELEMENT + " ?" + IDENTIFIED_BY;

    private static final String STRING_ASSERT = "(reads|returns|is|equals|contains|starts with|ends with|links to)";
    private static final String STRING_CONTENT = "\"([^\"]*)\"";

    public static final int TIMEOUT_IN_SECONDS = 5;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, By> aliases = new LinkedHashMap<>();
    private final DriverWrapper<WebDriver> driverWrapper;
    private final HTMLPage page;
    private WebElement element;

    public WebDriverSteps(DriverWrapper<WebDriver> driverWrapper) {
        this.driverWrapper = driverWrapper;
        page = new HTMLPage(driverWrapper.getDriver());
    }

    @After
    public void stopDriver() {
        driverWrapper.stopDriver();
    }

    // Helpers
    @When("^" + THAT + THE_USER + " know(?:s)? " + THE_ELEMENT_IDENTIFIED_BY + " as " + STRING_CONTENT + "$")
    public void that_we_know_the_element_named_as(String type, String id, String alias) {
        aliases.put(alias, by(type, id));
    }

    // Actions
    @When("^" + THAT + THE_USER + " navigate(?:s)? to " + STRING_CONTENT + "$")
    public void we_navigate_to(String url) {
        page.getDriver().get(url);
    }

    @When("^" + THAT + "the window is maximized$")
    public void the_window_is_maximized() {
        page.getDriver().manage().window().maximize();
    }

    @When("^" + THAT + THE_USER + " input(?:s)? \"([^\"]*)\" in " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void we_input_in_the_field_identified_by(String content, String type, String id) {
        find(by(type, id)).sendKeys(content);
    }

    @When("^" + THAT + THE_USER + " (?:press|enter)(?:[es])? (.*)$")
    public void that_we_press(Keys keys) {
        element.sendKeys(keys);
    }

    @When("^" + THAT + THE_USER + " click(?:s)? on " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void that_we_click_on_the_element_identified_by(String type, String id) {
        find(by(type, id)).click();
    }

    @When("^" + THAT + THE_USER + " clear(?:s)? " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void that_we_clear_the_element_identified_by(String type, String id) {
        find(by(type, id)).clear();
    }

    @When("^" + THAT + THE_USER + " wait(?:s)? (\\d+) (\\w+)")
    public void we_wait(int amount, TimeUnit timeUnit) throws InterruptedException {
        timeUnit.sleep(amount);
    }

    @When("^" + THAT + THE_USER + " select(?:s)? " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void we_select_the_element_identified_by(String type, String id) {
        find(by(type, id));
    }

    // Assertions
    @Then("^the title " + STRING_ASSERT + " " + STRING_CONTENT + "$")
    public void the_title_matches(String assertType, String expectedValue) {
        assertString(assertType, get(() -> page.getTitle()), expectedValue);
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " " + STRING_ASSERT + " " + STRING_CONTENT + "$")
    public void that_the_element_at_id_matches(String type, String id, String assertType, String expectedValue) {
        assertElement(assertType, find(by(type, id)), expectedValue);
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " is present$")
    public void the_element_with_id_is_present(String type, String id) {
        find(by(type, id));
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " is displayed")
    public void the_element_with_id_is_displayed(String type, String id) {
        Assert.assertTrue(find(by(type, id)).isDisplayed());
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " is enabled")
    public void the_element_with_id_is_enabled(String type, String id) {
        Assert.assertTrue(find(by(type, id)).isEnabled());
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " is selected")
    public void the_element_with_id_is_selected(String type, String id) {
        Assert.assertTrue(find(by(type, id)).isSelected());
    }

    @Then("^the current url " + STRING_ASSERT + " " + STRING_CONTENT + "$")
    public void the_current_url_ends_with(String assertType, String expectedValue) {
        assertString(assertType, page.getDriver().getCurrentUrl(), expectedValue);
    }

    @Then("^executing \"([^\"]*)\" " + STRING_ASSERT + " \"?(.+)\"?$")
    public void executing_returns(String javascript, String assertType, String expectedValue) {
        String value = String.valueOf(((JavascriptExecutor) page.getDriver()).executeScript("return window.scrollY;"));
        assertString(assertType, value, expectedValue);
    }

    @Then("^this element " + STRING_ASSERT + " " + STRING_CONTENT + "$")
    public void this_element_matches(String assertType, String expectedValue) throws Throwable {
        assertElement(assertType, element, expectedValue);
    }

    @Then("^this element attribute \"([^\"]*)\" " + STRING_ASSERT + " " + STRING_CONTENT + "$")
    public void this_element_attribute_matches(String attribute, String assertType, String expectedValue) {
        assertString(assertType, element.getAttribute(attribute), expectedValue);
    }

    @Then("^this element property \"([^\"]*)\" " + STRING_ASSERT + " " + STRING_CONTENT + "$")
    public void this_element_property_matches(String property, String assertType, String expectedValue) {
        assertString(assertType, element.getCssValue(property), expectedValue);
    }

    // privates
    private WebElement find(By elementLocation) {
        page.driverFluentWait(TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(elementLocation));
        element = page.getDriver().findElement(elementLocation);
        return element;
    }

    private String get(Supplier<String> supplier) {
        return Try.toGet(supplier)
            .until(value -> !Strings.isNullOrEmpty(value))
            .delayRetriesBy(500)
            .nTimes(10);
    }

    private By by(String type, String value) {
        Preconditions.checkArgument(value != null);
        if (type == null) {
            if (aliases.containsKey(value)) {
                return aliases.get(value);
            } else {
                throw new IllegalArgumentException("unknown alias: " + value);
            }
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
                case "link text":
                    return By.linkText(value);
                case "partial link text":
                    return By.partialLinkText(value);
                case "tag":
                    return By.tagName(value);
                default:
                    throw new IllegalArgumentException("unknown parameter type: " + type + " value: " + value);
            }
        }
    }

    private void assertElement(String assertType, WebElement element, String expected) {
        switch (assertType) {
            case "links to":
                Assert.assertEquals(expected, element.getAttribute("href"));
                break;
            default:
                assertString(assertType, get(() -> element.getText()), expected);
        }
    }

    private void assertString(String assertType, String value, String expected) {
        switch (assertType) {
            case "reads":
            case "returns":
            case "is":
            case "equals":
                Assert.assertEquals(expected, value);
                break;
            case "contains":
                Assert.assertTrue("'" + value + "' doesn't contain '" + expected + "'", value.contains(expected));
                break;
            case "starts with":
                Assert.assertTrue("'" + value + "' doesn't start with '" + expected + "'", value.startsWith(expected));
                break;
            case "ends with":
                Assert.assertTrue("'" + value + "' doesn't end with '" + expected + "'", value.endsWith(expected));
                break;
            default:
                throw new IllegalArgumentException("unknown assert type " + assertType);
        }
    }

}
