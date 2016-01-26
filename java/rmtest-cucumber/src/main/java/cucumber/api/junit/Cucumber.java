package cucumber.api.junit;

import java.io.IOException;
import java.util.*;

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
import gherkin.formatter.model.Step;
import se.redmind.rmtest.cucumber.utils.Tags;
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
 *
 * this class has been extended in place because we needed to be able to forward the parameters in the parameterized runtime
 *
 * @see CucumberOptions
 */
public class Cucumber extends ParentRunner<FeatureRunner> {

    static {
        Tags.addIgnoreToSystemProperties();
        Tags.addParameterizedToSystemProperties();
    }

    private final JUnitReporter jUnitReporter;
    private final List<FeatureRunner> children = new ArrayList<>();
    private final ParameterizableRuntime runtime;
    private final String name;
    // do not remove this field, it is read through reflection
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
        runtime = new ParameterizableRuntime(resourceLoader, new ResourceLoaderClassFinder(resourceLoader, classLoader), classLoader, runtimeOptions, name, parameters);

        final List<CucumberFeature> cucumberFeatures = runtime.cucumberFeatures();
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
            appendParameterizedName(featureRunner);
            children.add(featureRunner);
        }
    }

    private void appendParameterizedName(FeatureRunner featureRunner) throws InitializationError {
        List<ParentRunner<?>> runners = Fields.getSafeValue(featureRunner, "children");
        for (int i = 0; i < runners.size(); i++) {
            ParentRunner<?> runner = runners.get(i);
            if (runner instanceof ExecutionUnitRunner) {
                runner = new ExecutionUnitRunner(runtime, Fields.getSafeValue(runner, "cucumberScenario"), jUnitReporter) {
                    @Override
                    public Description getDescription() {
                        Description description = super.getDescription();
                        if (!description.getClassName().equals(Cucumber.this.getTestClass().getJavaClass().getCanonicalName())) {
                            Fields.set(description, "fDisplayName", description.getDisplayName()
                                + (name != null ? name : "") + "(" + Cucumber.this.getTestClass().getJavaClass().getCanonicalName() + ")");
                        }
                        return description;
                    }

                    @Override
                    protected Description describeChild(Step step) {
                        Description description = super.describeChild(step);
                        if (!description.getMethodName().contains("#" + step.getLine())) {
                            Fields.set(description, "fDisplayName", description.getMethodName() + "#" + step.getLine() + (name != null ? name : "") + "(" + description.getClassName() + ")");
                        }
                        return description;
                    }
                };
                runners.set(i, runner);
            }
        }
    }
}
