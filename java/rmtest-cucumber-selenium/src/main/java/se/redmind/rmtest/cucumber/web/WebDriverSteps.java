package se.redmind.rmtest.cucumber.web;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import se.redmind.rmtest.WebDriverWrapper;
import se.redmind.utils.Try;

/**
 * @author Jeremy Comte
 */
public class WebDriverSteps {

    public static final String THAT = "(?:that )?";
    public static final String THE_USER = "(?:.*)?";
    public static final String THE_ELEMENT = "(?:(?:the |an |a )?(?:button|element|field|checkbox|radio|value)?)?";
    public static final String DO_SOMETHING = "(click|clear|submit|select)(?:s? (?:on|in))?";
    public static final String INPUT = "(?:input|type)(?:s? (?:on|in))?";
    public static final String IDENTIFIED_BY = "(?:with (?:the )?)?(name(?:d)?|id|xpath|class|css|(?:partial )?link text|tag)? ?\"(.*)\"";
    public static final String THE_ELEMENT_IDENTIFIED_BY = THE_ELEMENT + " ?" + IDENTIFIED_BY;
    public static final String THIS_ELEMENT = "(?:(?:this|the|an) element(?:s)?|it(?:s)?)";
    public static final String MATCHES = "(reads|returns|is|equals|contains|starts with|ends with|links to|matches)";
    public static final String QUOTED_CONTENT = "\"([^\"]*)\"";

    private static final Pattern ALIAS = Pattern.compile("(.*)(?:\\$\\{(\\w+)\\})(.*)");

    public static final int TIMEOUT_IN_SECONDS = 5;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, By> aliasedLocations = new LinkedHashMap<>();
    private final Map<String, String> aliasedValues = new LinkedHashMap<>();
    private final WebDriverWrapper<WebDriver> driverWrapper;
    private final WebDriver driver;
    private WebElement element;
    private By elementLocation;

    public WebDriverSteps(WebDriverWrapper<WebDriver> driverWrapper) {
        this.driverWrapper = driverWrapper;
        this.driver = driverWrapper.getDriver();
    }

    public WebDriver getDriver() {
        return driver;
    }

    @After
    public void after() {
        if (!driverWrapper.reuseDriverBetweenTests()) {
            driverWrapper.stopDriver();
        }
    }

    // Helpers
    @When("^" + THAT + THE_USER + " know(?:s)? " + THE_ELEMENT_IDENTIFIED_BY + " as " + QUOTED_CONTENT + "$")
    public void that_we_know_the_element_named_as(String type, String id, String alias) {
        if (type != null && !type.equals("value")) {
            aliasedLocations.put(alias, by(type, id));
        } else {
            aliasedValues.put(alias, id);
        }
    }

    @Given("^th(?:is|ose|ese) alias(?:es)?:$")
    public void these_aliases(List<Map<String, String>> newAliases) {
        newAliases.forEach(alias -> aliasedLocations.put(alias.get("value"), by(alias.get("type"), alias.get("id"))));
    }

    @Given("^the aliases defined in the file \"([^\"]*)\"$")
    public void the_aliases_defined_in_the_file(String fileName) throws IOException {
        Splitter splitter = Splitter.on("|").trimResults().omitEmptyStrings();
        List<String> lines = Files.readLines(new File(fileName), Charset.defaultCharset());
        List<List<String>> rows = lines.stream().map(line -> splitter.splitToList(line)).collect(Collectors.toList());
        these_aliases(DataTable.create(rows).asMaps(String.class, String.class));
    }

    // Navigates
    @When("^" + THAT + THE_USER + " navigate(?:s)? to " + QUOTED_CONTENT + "$")
    public void we_navigate_to(String url) {
        driver.navigate().to(valueOf(url));
    }

    @When("^" + THAT + THE_USER + " (?:(?:go(?:es) )?(back|forward|refresh(?:es)?))$")
    public void we_navigate(String action) {
        switch (action) {
            case "back":
                driver.navigate().back();
                break;
            case "forward":
                driver.navigate().forward();
                break;
            case "refresh":
                driver.navigate().refresh();
                break;
        }
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
            driver.manage().addCookie(builder.build());
        });
    }

    @Given("^" + THAT + THE_USER + " delete(?:s)? the cookie \"([^\"]*)\"$")
    public void that_we_delete_the_cookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }

    @Given("^" + THAT + THE_USER + " delete(?:s)? all the cookies$")
    public void that_we_delete_all_the_cookies() {
        driver.manage().deleteAllCookies();
    }

    // Actions
    @When("^" + THAT + THE_USER + " " + DO_SOMETHING + " " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void that_we_do_something_on_the_element_identified_by(String action, String type, String id) {
        find(by(type, id));
        that_we_do_something_on_the_element_identified_by(action);
    }

    @When("^" + THAT + THE_USER + " " + DO_SOMETHING + " " + THIS_ELEMENT + "$")
    public void that_we_do_something_on_the_element_identified_by(String action) {
        switch (action) {
            case "click":
			waitForCondition(ExpectedConditions.elementToBeClickable(element));
                action().moveToElement(element).click().perform();
                break;
            case "clear":
                element.clear();
                break;
            case "submit":
                element.submit();
                break;
        }
    }

    @Given("^" + THAT + THE_USER + " maximize(?:s)? the window$")
    public void that_we_maximize_the_window() throws InterruptedException {
        the_window_is_maximized();
    }

    @When("^" + THAT + "the window is maximized$")
    public void the_window_is_maximized() throws InterruptedException {
        driver.manage().window().maximize();
        if (System.getProperty("os.name").startsWith("Mac")) {
            driverWrapper.getDriver().manage().window().setSize(new Dimension(1280, 950));
        }
    }

    @When("^" + THAT + THE_USER + " (?:press[es]?)(?:[es])? (.*)$")
    public void that_we_press(Keys keys) {
        element.sendKeys(keys);
    }

    @When("^" + THAT + THE_USER + " " + INPUT + " " + QUOTED_CONTENT + "$")
    public void that_we_input(String content) {
        element.sendKeys(content);
    }

    @When("^" + THAT + THE_USER + " " + INPUT + " " + QUOTED_CONTENT + " in " + THE_ELEMENT_IDENTIFIED_BY + "$")
    public void that_we_input_in_the_element_identified_by(String content, String type, String id) {
        find(by(type, id)).sendKeys(content);
    }

    @When("^" + THAT + THE_USER + " wait(?:s)? (\\d+) (\\w+)")
    public void we_wait(int amount, TimeUnit timeUnit) throws InterruptedException {
        timeUnit.sleep(amount);
    }

    // Assertions
    @Then("^the title " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void the_title_matches(String assertType, String expectedValue) {
        assertString(assertType, getNotNullOrEmpty(() -> driver.getTitle()), expectedValue);
    }

    @Then("^the page content " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void the_page_content_is(String assertType, String expectedValue) {
        assertString(assertType, find(By.tagName("body")).findElement(By.tagName("pre")).getText(), expectedValue);
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void that_the_element_at_id_matches(String type, String id, String assertType, String expectedValue) {
        assertElement(assertType, find(by(type, id)), valueOf(expectedValue));
    }

    @Then("^" + THE_ELEMENT_IDENTIFIED_BY + " (?:is present|exists)$")
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
        assertString(assertType, driver.getCurrentUrl(), expectedValue);
    }

    @Then("^executing " + QUOTED_CONTENT + " " + MATCHES + " \"?(.+)\"?$")
    public void executing_returns(String javascript, String assertType, String expectedValue) {
        String value = String.valueOf(((JavascriptExecutor) driver).executeScript("return window.scrollY;"));
        assertString(assertType, value, expectedValue);
    }

    @Then("^" + THAT + THIS_ELEMENT + " " + MATCHES + " " + QUOTED_CONTENT + "$")
    public void this_element_matches(String assertType, String expectedValue) {
        refreshElement();
        assertElement(assertType, element, valueOf(expectedValue));
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
        this.elementLocation = elementLocation;
        driverWrapper.driverFluentWait(TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(elementLocation));
        element = driver.findElement(elementLocation);
        return element;
    }
    
    private <T> void waitForCondition(ExpectedCondition<T> condition) {
		new WebDriverWait(driver, TIMEOUT_IN_SECONDS).until(condition);
	}

    private void refreshElement() {
        find(elementLocation);
    }

    private Actions action() {
        return new Actions(driver);
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

    private String valueOf(String value) {
        if (aliasedValues.containsKey(value)) {
            value = aliasedValues.get(value);
        }
        Matcher matcher;
        while ((matcher = ALIAS.matcher(value)).matches()) {
            value = matcher.group(1) + valueOf(matcher.group(2)) + matcher.group(3);
        }
        return value;
    }

    private By by(String type, String id) {
        Preconditions.checkArgument(id != null);
        if (type == null) {
            if (aliasedLocations.containsKey(id)) {
                return aliasedLocations.get(id);
            } else {
                throw new IllegalArgumentException("unknown alias: " + id);
            }
        } else {
            switch (type) {
                case "named":
                    return By.name(id);
                case "id":
                    return By.id(id);
                case "xpath":
                    return By.xpath(id);
                case "class":
                    return By.className(id);
                case "css":
                    return By.cssSelector(id);
                case "link text":
                    return By.linkText(id);
                case "partial link text":
                    return By.partialLinkText(id);
                case "tag":
                    return By.tagName(id);
                default:
                    throw new IllegalArgumentException("unknown parameter type: " + type + " value: " + id);
            }
        }
    }

    private void assertElement(String assertType, WebElement element, String expected) {
        switch (assertType) {
            case "links to":
                Assert.assertEquals(expected, element.getAttribute("href"));
                break;
            default:
                String value;
                if (element.getTagName().equals("input")) {
                    value = element.getAttribute("value");
                } else {
                    value = getNotNullOrEmpty(() -> element.getText());
                }
                assertString(assertType, value, expected);
                break;
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