package se.redmind.rmtest.selenium.livestream;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gherkin.formatter.model.Scenario;
import se.redmind.rmtest.selenium.livestream.RmTestResultBuilder;

public class RmTestResultBuilderTests {

    @Test
    public void propertiesTest() {
        String propertyName = "test.property.name";
        String propertyValue = "test.property.value";
        System.setProperty(propertyName, propertyValue);
        RmTestResultBuilder resultBuilder = getResultBuilder();
        JsonObject build = resultBuilder.build();
        JsonObject properties = build.get("properties").getAsJsonObject();
        Set<Entry<String, JsonElement>> reportPropertiesEtries = properties.entrySet();
        assertEquals(System.getProperties().size(), reportPropertiesEtries.size());

        String customProperty = properties.get(propertyName).getAsString();
        assertEquals(customProperty, propertyValue);
    }

    @Test
    public void addTestWithOkName() {
        RmTestResultBuilder resultBuilder = getResultBuilder();
        String test1 = "testGoogle1[OSX_UNKNOWN_AnApple_chrome_UNKNOWN](se.redmind.rmtest.selenium.example.GoogleExample)";
        String test2 = "testGoogle2[OSX_UNKNOWN_AnApple_chrome_UNKNOWN](se.redmind.rmtest.selenium.example.GoogleExample)";
        String test3 = "testGoogle3[OSX_UNKNOWN_AnApple_chrome_UNKNOWN](se.redmind.rmtest.selenium.example.GoogleExample)";
        resultBuilder.addTest(test1, Description.createSuiteDescription("testMethod1(se.redmind.rmtest.LiveStream)"));
        resultBuilder.addTest(test2, Description.createSuiteDescription("testMethod2(se.redmind.rmtest.LiveStream)"));
        resultBuilder.addTest(test3, Description.createSuiteDescription("testMethod3(se.redmind.rmtest.LiveStream)"));
        resultBuilder.addFinishedTest(test1);
        resultBuilder.addTestFailure(test2, new Failure(Description.createSuiteDescription(this.getClass()), new NullPointerException()));
        resultBuilder.addIgnoredTest(test3);
        JsonObject report = resultBuilder.build();
        int totalTests = report.get("totalTests").getAsInt();
        assertEquals(3, totalTests);
        JsonArray tests = report.get("tests").getAsJsonArray();
        assertEquals(3, tests.size());
        //Tests test1 - should be passed
        JsonObject testResult = tests.get(0).getAsJsonObject();
        String result;
        int id;
        result = testResult.get("result").getAsString();
        id = testResult.get("id").getAsInt();
        assertEquals(1, id);
        assertEquals("passed", result);

        //Tests test2 - should be failure
        testResult = tests.get(1).getAsJsonObject();
        id = testResult.get("id").getAsInt();
        result = testResult.get("result").getAsString();
        assertEquals(2, id);
        assertEquals("failure", result);

        //Tests test3 - should be skipped
        testResult = tests.get(2).getAsJsonObject();
        id = testResult.get("id").getAsInt();
        result = testResult.get("result").getAsString();
        assertEquals(3, id);
        assertEquals("skipped", result);
    }

    @Test
    public void testAssumptionFail() {
        RmTestResultBuilder resultBuilder = getResultBuilder();
        String test1 = "testGoogle1[OSX_UNKNOWN_AnApple_chrome_UNKNOWN](se.redmind.rmtest.selenium.example.GoogleExample)";
        resultBuilder.addTest(test1, Description.createTestDescription(LiveStreamListenerTest.class, "testAssumptionFail"));
        resultBuilder.addAssumptionFailure(test1, new Failure(Description.createSuiteDescription(this.getClass()), new NullPointerException()));
        JsonObject test = resultBuilder.getTest(test1);
        String result = test.get(RmTestResultBuilder.RESULT).getAsString();
        assertEquals("skipped", result);
    }

    @Test
    public void testIsGherkin() {
        RmTestResultBuilder resultBuilder = getResultBuilder();
        Description description = Description.createTestDescription(
            "se.redmind.rmtest.LiveStream",
            "testMethod",
            new Scenario(new ArrayList<>(), new ArrayList<>(), "Scenario: ", "This is a scenario", "", 1, "1")
        );

        assertTrue(resultBuilder.isGherkin(description));
    }

    @Test
    public void testIsNotGherkin() {
        boolean gherkin = getResultBuilder().isGherkin(Description.createTestDescription(LiveStreamListenerTest.class, "testIsNotGherkin"));
        assertFalse(gherkin);
    }

    private RmTestResultBuilder getResultBuilder() {
        return new RmTestResultBuilder();
    }

}