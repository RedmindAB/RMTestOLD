package se.redmind.rmtest.selenium.livestream.test;

import gherkin.formatter.model.Scenario;
import org.junit.runner.Description;
import se.redmind.utils.Fields;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by victormattsson on 2016-02-05.
 */
public class DescriptionMocker {
    public static Description mockDescription() {
        Description description = mock(Description.class);
        when(description.getDisplayName()).thenReturn("se.redmind.rmtest");
        when(description.getClassName()).thenReturn("se.redmind.rmtest.LiveStream");
        when(description.getMethodName()).thenReturn("testMethod()");

        return description;
    }

    public static void addFieldUniqueId(Description description) {
        Fields.set(description, "fUniqueId", new Scenario(new ArrayList<>(), new ArrayList<>(), "Scenario: ", "This " +
                "is a scenario", "", 1, "1"));
    }
}
