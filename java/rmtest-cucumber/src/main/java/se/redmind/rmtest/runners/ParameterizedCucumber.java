package se.redmind.rmtest.runners;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import cucumber.api.junit.Cucumber;
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
import cucumber.runtime.model.ParameterizedStep;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;
import se.redmind.rmtest.cucumber.utils.Tags;
import se.redmind.utils.Fields;
import se.redmind.utils.Methods;

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

    @Override
    public List<FeatureRunner> getChildren() {
        List<FeatureRunner> children = super.getChildren();

        Runtime runtime = Fields.getSafeValue(this, "runtime");
        JUnitReporter jUnitReporter = Fields.getSafeValue(this, "jUnitReporter");
        RuntimeGlue glue = (RuntimeGlue) runtime.getGlue();

        Map<Pattern, Pair<CucumberTagStatement, ParameterizedJavaStepDefinition>> parameterizedScenarios = new LinkedHashMap<>();

        // 1. Get the children from the parent class and intercept any parameterized scenario, wrap them and add them to the glue
        children.forEach(child -> {
            CucumberFeature feature = Fields.getSafeValue(child, "cucumberFeature");
            List<ParentRunner<?>> runners = Fields.getSafeValue(child, "children");
            List<CucumberTagStatement> statements = feature.getFeatureElements();
            for (int i = 0; i < statements.size(); i++) {
                CucumberTagStatement statement = statements.get(i);
                if (Tags.isParameterized(statement)) {
                    ParameterizedJavaStepDefinition parameterizedJavaStepDefinition = ParameterizedJavaStepDefinition.from(statement, jUnitReporter, runtime);
                    glue.addStepDefinition(parameterizedJavaStepDefinition);
                    parameterizedScenarios.put(Pattern.compile(parameterizedJavaStepDefinition.getPattern()), Pair.of(statements.remove(i), parameterizedJavaStepDefinition));
                    runners.remove(i);
                    i--;
                }
            }
        });

        // 2. Iterate over all the normal steps, and if the scenario is not quiet, rewrite and add the parameterized steps as normal steps.
        if (!parameterizedScenarios.isEmpty()) {
            PicoFactory picoFactory = getPicoFactory(runtime);
            picoFactory.addInstance(this);
            glue.addStepDefinition(new ParameterizedJavaStepDefinition(Methods.findMethod(this.getClass(), "endOfParameterizedScenario"), Pattern.compile("}"), 0, picoFactory, new String[0]));
            children.forEach(child -> {
                CucumberFeature feature = Fields.getSafeValue(child, "cucumberFeature");
                List<CucumberTagStatement> statements = feature.getFeatureElements();
                int modifiedSteps;
                // we need to keep trying as long as we find new parameterizable steps in order to support composite sub scenarios
                do {
                    modifiedSteps = 0;
                    for (CucumberTagStatement statement : statements) {
                        for (int i = 0; i < statement.getSteps().size(); i++) {
                            Step step = statement.getSteps().get(i);
                            if (step instanceof ParameterizedStep) {
                                if (((ParameterizedStep) step).getType() != ParameterizedStep.Type.Parameterized) {
                                    continue;
                                }
                            }

                            for (Map.Entry<Pattern, Pair<CucumberTagStatement, ParameterizedJavaStepDefinition>> parameterizedScenario : parameterizedScenarios.entrySet()) {
                                Matcher matcher = parameterizedScenario.getKey().matcher(step.getName());
                                if (matcher.matches() && !Tags.isQuiet(parameterizedScenario.getValue().getLeft())) {
                                    statement.getSteps().set(i, ParameterizedStep.startOf(step));
                                    String[] names = parameterizedScenario.getValue().getRight().parameters();
                                    Object[] parameters = new Object[names.length];
                                    for (int k = 0; k < names.length; k++) {
                                        parameters[k] = matcher.group(k + 1);
                                    }
                                    List<Step> newSteps = parameterizedScenario.getValue().getLeft().getSteps().stream()
                                        .map(parameterizedStep -> ParameterizedStep.parameterize(parameterizedStep, names, parameters))
                                        .collect(Collectors.toList());
                                    statement.getSteps().addAll(i + 1, newSteps);
                                    i += newSteps.size();
                                    statement.getSteps().add(++i, ParameterizedStep.endOf(step));
                                    modifiedSteps++;
                                }
                            }
                        }
                    }
                } while (modifiedSteps > 0);
            });
        }

        return children;
    }

    /**
     * this method is used as a target for the end of a parameterized scenario
     */
    public void endOfParameterizedScenario() {
    }

    /**
     * The way the Runtime is created by Cucumber prevents us to extract properly what would be a ParameterizedRuntime classes. Indeed, the Runtime is created
     * in the constructor of the Cucumber instance, so any local member would not be initialized when we need it. For that reason, we rely here on using the
     * effectively final parameter "optionalParameters".
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
