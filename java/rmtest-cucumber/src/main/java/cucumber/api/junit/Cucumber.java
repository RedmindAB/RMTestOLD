package cucumber.api.junit;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import gherkin.formatter.Reporter;
import gherkin.formatter.model.Result;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import cucumber.api.CucumberOptions;
import cucumber.runtime.*;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.ExecutionUnitRunner;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.*;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;
import se.redmind.utils.Fields;

/**
 * <p>
 * Classes annotated with {@code @RunWith(Cucumber.class)} will run a Cucumber Feature. The class should be empty without any fields or methods.
 * </p>
 * <p>
 * Cucumber will look for a {@code .feature} file on the classpath, using the same resource path as the annotated class ({@code .class} substituted by
 * {@code .feature}).
 * </p>
 * Additional hints can be given to Cucumber by annotating the class with {@link CucumberOptions}.
 * <p>
 * this class has been extended in place because we needed to be able to forward the parameters in the parameterized runtime
 *
 * @see CucumberOptions
 */
public class Cucumber extends ParentRunner<FeatureRunner> {

    private final JUnitReporter jUnitReporter;
    private final List<FeatureRunner> children = new ArrayList<>();
    private final ParameterizableRuntime runtime;
    private final String name;
    private final boolean useRealClassnamesForSurefire;
    private final boolean reportScenariosOnlyInSurefire;
    // do not remove this field, it is read through reflection
    private final Object[] parameters;

    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     * @throws java.io.IOException                         if there is a problem
     * @throws org.junit.runners.model.InitializationError if there is another problem
     */
    public Cucumber(Class clazz) throws InitializationError, IOException {
        this(clazz, null, new Object[0]);
    }

    public Cucumber(Class clazz, String name, Object... parameters) throws InitializationError, IOException {
        super(clazz);
        this.name = name;
        this.parameters = parameters;

        useRealClassnamesForSurefire = "true".equals(System.getProperty("useRealClassnamesForSurefire"));
        reportScenariosOnlyInSurefire = "true".equals(System.getProperty("reportScenariosOnlyInSurefire"));

        ClassLoader classLoader = clazz.getClassLoader();
        Assertions.assertNoCucumberAnnotatedMethods(clazz);

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        runtime = new ParameterizableRuntime(resourceLoader, new ResourceLoaderClassFinder(resourceLoader, classLoader), classLoader, runtimeOptions, name, parameters);

        final List<CucumberFeature> cucumberFeatures = runtime.cucumberFeatures();
        Reporter reporter = runtimeOptions.reporter(classLoader);
        jUnitReporter = new JUnitReporter(reporter, runtimeOptions.formatter(classLoader), runtimeOptions.isStrict()) {

            public void result(Result result) {
                if (reportScenariosOnlyInSurefire) {
                    Throwable error = result.getError();
                    if (error != null) {
                        EachTestNotifier executionUnitNotifier = Fields.getSafeValue(this, "executionUnitNotifier");
                        if (executionUnitNotifier != null) {
                            executionUnitNotifier.addFailure(error);
                        }
                    }
                    reporter.result(result);
                } else {
                    super.result(result);
                }
            }

        };
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
            if (!cucumberFeature.getFeatureElements().isEmpty()) {
                FeatureRunner featureRunner = new FeatureRunner(cucumberFeature, runtime, jUnitReporter);
                appendParameterizedName(featureRunner);
                children.add(featureRunner);
            }
        }
    }

    private void appendParameterizedName(FeatureRunner featureRunner) throws InitializationError {
        List<ParentRunner<?>> runners = Fields.getSafeValue(featureRunner, "children");
        CucumberFeature cucumberFeature = Fields.getSafeValue(featureRunner, "cucumberFeature");
        String featureName = ((String) Fields.getSafeValue(cucumberFeature, "path")).replaceFirst(".feature$", "").replaceAll("/", ".");
        Map<String, StepDefinition> stepDefinitionsByPattern = Fields.getSafeValue(runtime.getGlue(), "stepDefinitionsByPattern");
        for (int i = 0; i < runners.size(); i++) {
            ParentRunner<?> runner = runners.get(i);
            if (runner instanceof ExecutionUnitRunner) {
                CucumberScenario cucumberScenario = Fields.getSafeValue(runner, "cucumberScenario");
                Scenario scenario = Fields.getSafeValue(cucumberScenario, "scenario");
                runner = new ExecutionUnitRunner(runtime, cucumberScenario, jUnitReporter) {
                    @Override
                    public Description getDescription() {
                        Description description = super.getDescription();
                        if (useRealClassnamesForSurefire && !description.getClassName().equals(featureName)) {
                            Fields.set(description, "fDisplayName", featureName + ":" + scenario.getLine() + (name != null ? name : "") + "(" + Cucumber.this.getTestClass().getJavaClass().getName() + ")");
                        }
                        return description;
                    }

                    @Override
                    protected Description describeChild(Step step) {
                        Description description = super.describeChild(step);
                        if (!description.getMethodName().contains("#" + step.getLine())) {
                            Method method = findMatchingMethod(step);
                            if (useRealClassnamesForSurefire && method != null) {
                                Fields.set(description, "fDisplayName", method.getName() + "@" + featureName + "#" + step.getLine() + (name != null ? name : "") + "(" + method.getDeclaringClass().getName() + ")");
                            } else {
                                Fields.set(description, "fDisplayName", description.getMethodName() + "#" + step.getLine() + (name != null ? name : "") + "(" + description.getClassName() + ")");
                            }
                        }
                        return description;
                    }

                    private Method findMatchingMethod(Step step) {
                        Method method = null;
                        for (Map.Entry<String, StepDefinition> entry : stepDefinitionsByPattern.entrySet()) {
                            if (step.getName().matches(entry.getKey())) {
                                method = Fields.getSafeValue(entry.getValue(), "method");
                                break;
                            }
                        }
                        return method;
                    }

                };
                runners.set(i, runner);
            }
        }
    }
}
