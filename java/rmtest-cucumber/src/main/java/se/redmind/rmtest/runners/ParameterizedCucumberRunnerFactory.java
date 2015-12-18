package se.redmind.rmtest.runners;

import java.io.IOException;
import java.util.*;

import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import cucumber.runtime.Backend;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.java.picocontainer.PicoFactory;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Tag;
import se.redmind.rmtest.cucumber.utils.Tags;
import se.redmind.utils.Fields;

/**
 * Here we are just injecting our parameters in the picocontainer instance of this test.
 *
 * The way the Runtime is created by Cucumber prevents us to extract properly what would be the ParameterizedCucumber and ParameterizedRuntime classes. Indeed,
 * the Runtime is created in the constructor of the Cucumber instance, so any local member would not be initialized when we need it. For that reason, we rely
 * here on using the effectively final parameter "test".
 *
 * @author Jeremy Comte
 */
public class ParameterizedCucumberRunnerFactory implements ParametersRunnerFactory {

    static {
        Tags.addIgnoreToSystemProperties();
    }

    @Override
    public RMCucumber createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        try {
            return new RMCucumber(test.getTestClass().getJavaClass()) {

                protected final Object[] parameters = test.getParameters().toArray();

                @Override
                protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader, RuntimeOptions runtimeOptions) throws InitializationError, IOException {
                    return new Runtime(resourceLoader, new ResourceLoaderClassFinder(resourceLoader, classLoader), classLoader, runtimeOptions) {

                        @Override
                        public void buildBackendWorlds(Reporter reporter, Set<Tag> tags, Scenario gherkinScenario) {
                            PicoFactory picoFactory = getPicoFactory(this);
                            test.getParameters().forEach(parameter -> picoFactory.addInstance(parameter));
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
