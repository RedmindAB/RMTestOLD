package cucumber.runtime.model;

import gherkin.formatter.model.Step;
import se.redmind.utils.Fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jeremy Comte
 */
public class ParameterizedStepContainer extends StepContainer {

    public ParameterizedStepContainer(StepContainer stepContainer, String[] names, Object[] parameters) {
        super(stepContainer.cucumberFeature, Fields.getSafeValue(stepContainer, "statement"));

        stepContainer.getSteps().forEach(step -> {
            step(new Step(step.getComments(), step.getKeyword(), replacePlaceHolders(step.getName(), names, parameters), step.getLine(), step.getRows(), step.getDocString()));
        });
    }

    public static String replacePlaceHolders(String name, String[] names, Object[] parameters) {
        for (int i = 0; i < names.length; i++) {
            String value = String.valueOf(parameters[i]);
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            name = name.replaceAll("<" + Pattern.quote(names[i]) + ">", Matcher.quoteReplacement(value));
        }
        return name;
    }

}
