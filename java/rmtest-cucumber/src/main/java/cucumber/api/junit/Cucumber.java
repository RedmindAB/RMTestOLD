package cucumber.api.junit;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import cucumber.api.CucumberOptions;
import cucumber.runtime.*;
import cucumber.runtime.Runtime;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.java.ParameterizedJavaStepDefinition;
import cucumber.runtime.java.picocontainer.PicoFactory;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.ExecutionUnitRunner;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.*;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.TagStatement;
import se.redmind.rmtest.cucumber.utils.Tags;
import se.redmind.utils.Fields;
import se.redmind.utils.Methods;

/**
 * <p>
 * Classes annotated with {@code @RunWith(Cucumber.class)} will run a Cucumber Feature. The class should be empty without any fields or methods.
 * </p>
 * <p>
 * Cucumber will look for a {@code .feature} file on the classpath, using the same resource path as the annotated class ({@code .class} substituted by
 * {@code .feature}).
 * </p>
 * Additional hints can be given to Cucumber by annotating the class with {@link CucumberOptions}.
 *
 * this class has been extended in place because we needed to be able to forward the parameters in the parameterized runtime
 *
 * @see CucumberOptions
 */
public class Cucumber extends ParentRunner<FeatureRunner> {

    static {
        Tags.addIgnoreToSystemProperties();
    }

    private static enum CompositionType {

        InPlace, Full, Quiet
    }

    private final JUnitReporter jUnitReporter;
    private final List<FeatureRunner> children = new ArrayList<>();
    private final Runtime runtime;
    private final String name;
    private final Object[] parameters;

    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     * @throws java.io.IOException if there is a problem
     * @throws org.junit.runners.model.InitializationError if there is another problem
     */
    public Cucumber(Class clazz) throws InitializationError, IOException {
        this(clazz, null, new Object[0]);
    }

    public Cucumber(Class clazz, String name, Object... parameters) throws InitializationError, IOException {
        super(clazz);
        this.name = name;
        this.parameters = parameters;
        ClassLoader classLoader = clazz.getClassLoader();
        Assertions.assertNoCucumberAnnotatedMethods(clazz);

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        runtime = new ParameterizedRuntime(resourceLoader, new ResourceLoaderClassFinder(resourceLoader, classLoader), classLoader, runtimeOptions, parameters);

        final List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);
        jUnitReporter = new JUnitReporter(runtimeOptions.reporter(classLoader), runtimeOptions.formatter(classLoader), runtimeOptions.isStrict());
        addChildren(cucumberFeatures);
    }

    @Override
    public List<FeatureRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(FeatureRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {
        child.run(notifier);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        jUnitReporter.done();
        jUnitReporter.close();
        runtime.printSummary();
    }

    private void addChildren(List<CucumberFeature> cucumberFeatures) throws InitializationError {
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            FeatureRunner featureRunner = new FeatureRunner(cucumberFeature, runtime, jUnitReporter);
            if (name != null) {
                // if we have a name, we want to append it to the Description
                List<ParentRunner<?>> runners = Fields.getSafeValue(featureRunner, "children");
                for (int i = 0; i < runners.size(); i++) {
                    ParentRunner<?> runner = runners.get(i);
                    if (runner instanceof ExecutionUnitRunner) {
                        runner = new ExecutionUnitRunner(runtime, Fields.getSafeValue(runner, "cucumberScenario"), jUnitReporter) {

                            @Override
                            protected Description describeChild(Step step) {
                                Description description = super.describeChild(step);
                                if (!description.getMethodName().contains(name)) {
                                    Fields.set(description, "fDisplayName", description.getMethodName() + name + "(" + description.getClassName() + ")");
                                }
                                return description;
                            }
                        };
                        runners.set(i, runner);
                    }
                }
            }
            children.add(featureRunner);
        }

        RuntimeGlue glue = (RuntimeGlue) runtime.getGlue();

        Map<Pattern, ParameterizedJavaStepDefinition.Factory> parameterizedScenarios = new LinkedHashMap<>();

        // 1. Get the children from the parent class and intercept any parameterized scenario and instanciate their factories
        children.forEach(child -> {
            CucumberFeature feature = Fields.getSafeValue(child, "cucumberFeature");
            List<ParentRunner<?>> runners = Fields.getSafeValue(child, "children");
            List<CucumberTagStatement> statements = feature.getFeatureElements();
            for (int i = 0; i < statements.size(); i++) {
                CucumberTagStatement statement = statements.get(i);
                if (Tags.isParameterized(statement)) {
                    ParameterizedJavaStepDefinition.Factory stepFactory = ParameterizedJavaStepDefinition.from(statement, jUnitReporter, runtime);
                    parameterizedScenarios.put(stepFactory.pattern(), stepFactory);
                    statements.remove(i);
                    runners.remove(i);
                    i--;
                } else if (name != null) {
                    TagStatement tagStatement = statement.getGherkinModel();
                    Fields.set(tagStatement, "name", tagStatement.getName() + " " + name);
                }
            }
        });

        // 2. Iterate over all the normal steps, and if the scenario is not quiet, rewrite and add the parameterized steps as normal steps.
        if (!parameterizedScenarios.isEmpty()) {
            PicoFactory picoFactory = getPicoFactory(runtime);
            picoFactory.addInstance(this);
            glue.addStepDefinition(new ParameterizedJavaStepDefinition(Methods.findMethod(this.getClass(), "endOfParameterizedScenario"), Pattern.compile("}"), 0, picoFactory));
            children.forEach(child -> {
                CucumberFeature feature = Fields.getSafeValue(child, "cucumberFeature");
                List<StepContainer> stepContainers = new ArrayList<>(feature.getFeatureElements());

                CucumberBackground cucumberBackground = Fields.getSafeValue(feature, "cucumberBackground");
                if (cucumberBackground != null) {
                    stepContainers.add(cucumberBackground);
                }

                int modifiedSteps;
                // we need to keep trying as long as we find new parameterizable steps in order to support composite sub scenarios
                do {
                    modifiedSteps = 0;
                    for (StepContainer stepContainer : stepContainers) {
                        for (int i = 0; i < stepContainer.getSteps().size(); i++) {
                            Step step = stepContainer.getSteps().get(i);

                            if (step instanceof ParameterizedStep) {
                                if (((ParameterizedStep) step).getType() == ParameterizedStep.Type.Start
                                    || ((ParameterizedStep) step).getType() == ParameterizedStep.Type.Quiet) {
                                    continue;
                                }
                            }

                            String stepName = step.getName();
                            CompositionType compositionType = CompositionType.InPlace;
                            if (stepName.contains(Tags.QUIET)) {
                                compositionType = CompositionType.Quiet;
                                stepName = stepName.replaceAll(Tags.QUIET, "").trim();
                            } else if (stepName.contains(Tags.FULL)) {
                                compositionType = CompositionType.Full;
                                stepName = stepName.replaceAll(Tags.FULL, "").trim();
                            }

                            for (Map.Entry<Pattern, ParameterizedJavaStepDefinition.Factory> parameterizedScenario : parameterizedScenarios.entrySet()) {
                                Matcher matcher = parameterizedScenario.getKey().matcher(stepName);
                                if (matcher.matches()) {
                                    if (compositionType == CompositionType.Quiet) {
                                        stepContainer.getSteps().set(i, ParameterizedStep.asQuiet(step));
                                        parameterizedScenario.getValue().addQuietSubStepsToGlue();
                                    } else {
                                        Function<Step, ParameterizedStep> wrapper;
                                        String[] names = parameterizedScenario.getValue().parameters();
                                        Object[] scenarioParameters = new Object[names.length];
                                        for (int k = 0; k < names.length; k++) {
                                            scenarioParameters[k] = matcher.group(k + 1);
                                        }
                                        if (compositionType == CompositionType.Full) {
                                            parameterizedScenario.getValue().addStartStepToGlue();
                                            stepContainer.getSteps().set(i, ParameterizedStep.startOf(step));
                                            wrapper = parameterizedStep -> ParameterizedStep.asSubStep(parameterizedStep, names, scenarioParameters);
                                        } else {
                                            stepContainer.getSteps().remove(i--);
                                            wrapper = parameterizedStep -> ParameterizedStep.parameterize(parameterizedStep, names, scenarioParameters);
                                        }

                                        List<Step> newSteps = parameterizedScenario.getValue().statement().getSteps().stream()
                                            .map(parameterizedStep -> wrapper.apply(parameterizedStep))
                                            .collect(Collectors.toList());
                                        stepContainer.getSteps().addAll(i + 1, newSteps);
                                        i += newSteps.size();
                                        if (compositionType == CompositionType.Full) {
                                            stepContainer.getSteps().add(++i, ParameterizedStep.endOf(step));
                                        }
                                        modifiedSteps++;
                                    }

                                }
                            }
                        }
                    }
                } while (modifiedSteps > 0);
            });
        }
    }

    /**
     * this method is used as a target for the end of a parameterized scenario
     */
    public void endOfParameterizedScenario() {
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
