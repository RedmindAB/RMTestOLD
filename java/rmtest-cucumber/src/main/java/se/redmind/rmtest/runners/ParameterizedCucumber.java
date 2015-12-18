package se.redmind.rmtest.runners;

import cucumber.api.junit.Cucumber;

import java.io.IOException;
import java.util.*;

import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import cucumber.runtime.Backend;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeGlue;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.java.ParameterizedJavaStepDefinition;
import cucumber.runtime.java.picocontainer.PicoFactory;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Tag;
import se.redmind.rmtest.cucumber.utils.Tags;
import se.redmind.utils.Fields;

/**
 * @author Jeremy Comte
 */
public class ParameterizedCucumber extends Cucumber {

    static {
        Tags.addIgnoreToSystemProperties();
    }

    public ParameterizedCucumber(Class<?> clazz) throws InitializationError, IOException {
        super(clazz);
    }

    /**
     * Get the children from the parent class and intercept any parameterized scenario, wrap them and add them to the glue
     *
     * @return the children
     */
    @Override
    public List<FeatureRunner> getChildren() {
        List<FeatureRunner> children = super.getChildren();

        Runtime runtime = Fields.getSafeValue(this, "runtime");
        JUnitReporter jUnitReporter = Fields.getSafeValue(this, "jUnitReporter");
        RuntimeGlue glue = (RuntimeGlue) runtime.getGlue();

        children.forEach(child -> {
            CucumberFeature feature = Fields.getSafeValue(child, "cucumberFeature");
            List<ParentRunner<?>> runners = Fields.getSafeValue(child, "children");
            List<CucumberTagStatement> statements = feature.getFeatureElements();
            for (int i = 0; i < statements.size(); i++) {
                CucumberTagStatement statement = statements.get(i);
                if (statement.getGherkinModel().getTags().stream().anyMatch(tag -> "@parameterized".equals(tag.getName()))) {
                    glue.addStepDefinition(ParameterizedJavaStepDefinition.from(statement, jUnitReporter, runtime));
                    statements.remove(i);
                    runners.remove(i);
                    i--;
                }
            }
        });

        return children;
    }

    /**
     * The way the Runtime is created by Cucumber prevents us to extract properly what would be a ParameterizedRuntime classes.
     * Indeed, the Runtime is created in the constructor of the Cucumber instance, so any local member would not be initialized when we need it. For that
     * reason, we rely here on using the effectively final parameter "optionalParameters".
     */
    public static ParameterizedCucumber create(Class<?> clazz, Object... optionalParameters) throws InitializationError {
        try {
            return new ParameterizedCucumber(clazz) {

                protected final Object[] parameters = optionalParameters;

                @Override
                protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader, RuntimeOptions runtimeOptions) throws InitializationError, IOException {
                    return new Runtime(resourceLoader, new ResourceLoaderClassFinder(resourceLoader, classLoader), classLoader, runtimeOptions) {

                        @Override
                        public void buildBackendWorlds(Reporter reporter, Set<Tag> tags, Scenario gherkinScenario) {
                            PicoFactory picoFactory = getPicoFactory(this);
                            for (Object parameter : optionalParameters) {
                                picoFactory.addInstance(parameter);
                            }
                            super.buildBackendWorlds(reporter, tags, gherkinScenario);
                        }
                    };
                }

            };
        } catch (IOException ex) {
            throw new InitializationError(ex);
        }
    }

    public static PicoFactory getPicoFactory(Runtime runtime) throws RuntimeException {
        Collection<? extends Backend> backends = Fields.getSafeValue(runtime, "backends");
        Optional<JavaBackend> first = backends.stream()
            .filter(backend -> backend instanceof JavaBackend)
            .map(backend -> (JavaBackend) backend)
            .findFirst();
        if (first.isPresent()) {
            return Fields.getSafeValue(first.get(), "objectFactory");
        }
        throw new RuntimeException("can't find a javaBackend instance");
    }

}
