package se.redmind.rmtest.cucumber;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import cucumber.api.junit.Cucumber;
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
import se.redmind.utils.Fields;

/**
 * Here we are just injecting our parameters in the picocontainer instance of this test
 * @author Jeremy Comte
 */
public class CucumberParametersRunnerFactory implements ParametersRunnerFactory {

    @Override
    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        try {
            return new Cucumber(test.getTestClass().getJavaClass()) {

                private boolean isInjected;

                @Override
                protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader, RuntimeOptions runtimeOptions) throws InitializationError, IOException {
                    return new Runtime(resourceLoader, new ResourceLoaderClassFinder(resourceLoader, classLoader), classLoader, runtimeOptions) {
                        @Override
                        public void buildBackendWorlds(Reporter reporter, Set<Tag> tags, Scenario gherkinScenario) {
                            PicoFactory picoFactory = getPicoFactory();
                            picoFactory.addInstance(test.getParameters().get(0));
                            super.buildBackendWorlds(reporter, tags, gherkinScenario);
                        }

                        private PicoFactory getPicoFactory() throws RuntimeException {
                            try {
                                Collection<? extends Backend> backends = Fields.getValue(this, "backends");
                                Optional<JavaBackend> first = backends.stream()
                                    .filter(backend -> backend instanceof JavaBackend)
                                    .map(backend -> (JavaBackend) backend)
                                    .findFirst();
                                if (first.isPresent()) {
                                    return Fields.getValue(first.get(), "objectFactory");
                                }
                                throw new RuntimeException("can't find a javaBackend instance");
                            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    };
                }

            };
        } catch (IOException ex) {
            throw new InitializationError(ex);
        }
    }

}
