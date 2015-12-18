package se.redmind.rmtest.runners;

import cucumber.api.junit.Cucumber;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeGlue;
import cucumber.runtime.java.ParameterizedJavaStepDefinition;
import cucumber.runtime.java.picocontainer.PicoFactory;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberTagStatement;
import se.redmind.rmtest.cucumber.utils.Tags;
import se.redmind.utils.Fields;

/**
 * @author Jeremy Comte
 */
public class RMCucumber extends Cucumber {

    static {
        Tags.addIgnoreToSystemProperties();
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RMCucumber(Class<?> clazz) throws InitializationError, IOException {
        super(clazz);
    }

    //CucumberScenario
    @Override
    public List<FeatureRunner> getChildren() {
        List<FeatureRunner> children = super.getChildren();

        Set<CucumberTagStatement> parameterizedStatements = new LinkedHashSet<>();
        Runtime runtime = Fields.getSafeValue(this, "runtime");
        JUnitReporter jUnitReporter = Fields.getSafeValue(this, "jUnitReporter");
        RuntimeGlue glue = (RuntimeGlue) runtime.getGlue();

        for (FeatureRunner child : children) {
            CucumberFeature feature = Fields.getSafeValue(child, "cucumberFeature");
            List<ParentRunner<?>> runners = Fields.getSafeValue(child, "children");
            List<CucumberTagStatement> statements = feature.getFeatureElements();
            for (int i = 0; i < statements.size(); i++) {
                CucumberTagStatement statement = statements.get(i);
                if (statement.getGherkinModel().getTags().stream().anyMatch(tag -> "@parameterized".equals(tag.getName()))) {
                    parameterizedStatements.add(statement);
                    glue.addStepDefinition(ParameterizedJavaStepDefinition.from(statement, jUnitReporter, runtime));
                    statements.remove(i);
                    runners.remove(i);
                    i--;
                }
            }
        }

        return children;
    }

}
