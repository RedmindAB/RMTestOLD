package se.redmind.rmtest.cucumber.web;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
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
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.selenium.framework.HTMLPage;
import se.redmind.rmtest.utils.Methods;
import se.redmind.utils.Try;

/**
 * @author Jeremy Comte
 */
public class WebDriverSteps {

    private static final String THAT = "(?:that )?";
    private static final String THE_USER = "(?:.*)?";
    private static final String THE_ELEMENT = "(?:the ?(?:button|element|field|checkbox|radio)?)?";
    private static final String DO_SOMETHING = "(click|clear|submit|select)(?:s? (?:on|in))?";
    private static final String INPUT = "(input|type)(?:s? (?:on|in))?";
    private static final String IDENTIFIED_BY = "(?:with )?(named|id|xpath|class|css|(?:partial )?link text|tag)? ?\"(.*)\"";
    private static final String THE_ELEMENT_IDENTIFIED_BY = THE_ELEMENT + " ?" + IDENTIFIED_BY;
    private static final String THIS_ELEMENT = "(?:this element|it(?:s)?)";

    private static final String MATCHES = "(reads|returns|is|equals|contains|starts with|ends with|links to|matches)";
    private static final String QUOTED_CONTENT = "\"([^\"]*)\"";

    private static final String TEXT_PREFIX = "<html><head></head><body><pre style=\"word-wrap: break-word; white-space: pre-wrap;\">";
    private static final String TEXT_SUFFIX = "</pre></body></html>";

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
    @When("^" + THAT + THE_USER + " know(?:s)? " + THE_ELEMENT_IDENTIFIED_BY + " as " + QUOTED_CONTENT + "$")
    public void that_we_know_the_element_named_as(String type, String id, String alias) {
        aliases.put(alias, by(type, id));
    }

    // Navigates
    @When("^" + THAT + THE_USER + " navigate(?:s)? to " + QUOTED_CONTENT + "$")
    public void we_navigate_to(String url) {
        page.getDriver().navigate().to(url);
    }

    @When("^" + THAT + THE_USER + " (?:(?:go(?:es) )?(back|forward|refresh(?:es)?))$")
    public void we_navigate(String action) {
        Methods.invoke(page.getDriver().navigate(), action);
    }

    // Cookie Options
    @Given("^" + THAT + THE_USER + " add(?:s)? th(?:is|ose) cookie(?:s)?:$")
    public void that_we_add_those_cookies(List<Map<String, String>> data) {
        data.forEach(cookieAsMap -> {
            Cookie.Builder builder = new Cookie.Builder(cookieAsMap.get("name"), cookieAsMap.get("value"));
            if (cookieAsMap.containsKey("domain")) {
                builder.domain(cookieAsMap.get("domain"));
            }
            if (cookieAsMap.containsKey("path")) {
                builder.path(cookieAsMap.get("path"));
            }
            if (cookieAsMap.containsKey("expiry")) {
                builder.expiresOn(Date.valueOf(cookieAsMap.get("expiry")));
            }
            if (cookieAsMap.containsKey("isSecure")) {
                builder.isSecure(Boolean.valueOf(cookieAsMap.get("isSecure")));
            }
            if (cookieAsMap.containsKey("isHttpOnly")) {
                builder.isHttpOnly(Boolean.valueOf(cookieAsMap.get("isHttpOnly")));
            }
            page.getDriver().manage().addCookie(builder.build());
        });
    }

    @Given("^" + THAT + THE_USER + " delete(?:s)? the cookie \"([^\"]*)\"$")
    public void that_we_delete_the_cookie(String name) {
        page.getDriver().manage().deleteCookieNamed(name);
    }

    @Given("^" + THAT + THE_USER + " delete(?:s)? all the cookies$")
    public void that_we_delete_all_the_cookies() {
        page.getDriver().manage().deleteAllCookies();
    }

    // Actions
    @When("^" + THAT + THE_USER + " " + DO_SOMETHING + " " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void that_we_do_something_on_the_element_identified_by(String action, String type, String id) {
        find(by(type, id));
        if (!"select".equals(action)) {
            Methods.invoke(element, action);
        }
    }

    @When("^" + THAT + "the window is maximized$")
    public void the_window_is_maximized() {
        page.getDriver().manage().window().maximize();
    }

    @When("^" + THAT + THE_USER + " (?:press[es]?)(?:[es])? (.*)$")
    public void that_we_press(Keys keys) {
        element.sendKeys(keys);
    }

    @When("^" + THAT + THE_USER + " wait(?:s)? (\\d+) (\\w+)")
    public void we_wait(int amount, TimeUnit timeUnit) throws InterruptedException {
        timeUnit.sleep(amount);
    }

    // Assertions
    @Then("^the title " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void the_title_matches(String assertType, String expectedValue) {
        assertString(assertType, getNotNullOrEmpty(() -> page.getTitle()), expectedValue);
    }

    @Then("^the page content " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void the_page_content_is(String assertType, String expectedValue) {
        assertString(assertType, page.getDriver().getPageSource().replaceAll(TEXT_PREFIX, "").replaceAll(TEXT_SUFFIX, ""), expectedValue);
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " " + MATCHES + " " + QUOTED_CONTENT + "$")
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

    @Then("^the current url " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void the_current_url_ends_with(String assertType, String expectedValue) {
        assertString(assertType, page.getDriver().getCurrentUrl(), expectedValue);
    }

    @Then("^executing " + QUOTED_CONTENT + " " + MATCHES + " \"?(.+)\"?$")
    public void executing_returns(String javascript, String assertType, String expectedValue) {
        String value = String.valueOf(((JavascriptExecutor) page.getDriver()).executeScript("return window.scrollY;"));
        assertString(assertType, value, expectedValue);
    }

    @Then("^" + THAT + THIS_ELEMENT + " " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void this_element_matches(String assertType, String expectedValue) {
        assertElement(assertType, element, expectedValue);
    }

    @Then("^" + THAT + THIS_ELEMENT + " is selected$")
    public void this_element_is_selected() {
        Assert.assertTrue(element.isSelected());
    }

    @Then("^" + THAT + THIS_ELEMENT + " attribute \"([^\"]*)\" " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void this_element_attribute_matches(String attribute, String assertType, String expectedValue) {
        assertString(assertType, element.getAttribute(attribute), expectedValue);
    }

    @Then("^the attribute " + QUOTED_CONTENT + " of " + THE_ELEMENT_IDENTIFIED_BY + " " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void the_attribute_of_the_element_identified_by_matches(String attribute, String type, String id, String assertType, String expectedValue) {
        find(by(type, id));
        this_element_attribute_matches(attribute, assertType, expectedValue);
    }

    @Then("^" + THAT + THIS_ELEMENT + " property \"([^\"]*)\" " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void this_element_property_matches(String property, String assertType, String expectedValue) {
        assertString(assertType, element.getCssValue(property), expectedValue);
    }

    @Then("^the property " + QUOTED_CONTENT + " of " + THE_ELEMENT_IDENTIFIED_BY + " " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void the_property_of_the_element_identified_by_matches(String property, String type, String id, String assertType, String expectedValue) {
        find(by(type, id));
        this_element_property_matches(property, assertType, expectedValue);
    }

    // private helpers
    private WebElement find(By elementLocation) {
        page.driverFluentWait(TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(elementLocation));
        element = page.getDriver().findElement(elementLocation);
        return element;
    }

    private String get(Supplier<String> supplier) {
        return Try.toGet(supplier)
            .delayRetriesBy(500)
            .nTimes(10);
    }

    private String getNotNullOrEmpty(Supplier<String> supplier) {
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
                assertString(assertType, getNotNullOrEmpty(() -> element.getText()), expected);
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
            case "matches":
                Assert.assertTrue("'" + value + "' doesn't match '" + expected + "'", value.matches(expected));
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
