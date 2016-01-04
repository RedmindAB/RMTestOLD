package cucumber.runtime;

import java.util.Set;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.Runtime;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.java.picocontainer.PicoFactory;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Tag;

/**
 * @author Jeremy Comte
 */
public class ParameterizedRuntime extends Runtime {

    private final Object[] parameters;

    public ParameterizedRuntime(ResourceLoader resourceLoader, ClassFinder classFinder, ClassLoader classLoader, RuntimeOptions runtimeOptions, Object[] parameters) {
        super(resourceLoader, classFinder, classLoader, runtimeOptions);
        this.parameters = parameters;
    }

    @Override
    public void buildBackendWorlds(Reporter reporter, Set<Tag> tags, Scenario gherkinScenario) {
        PicoFactory picoFactory = Cucumber.getPicoFactory(this);
        for (Object parameter : parameters) {
            picoFactory.addInstance(parameter);
        }
        super.buildBackendWorlds(reporter, tags, gherkinScenario);
    }

}
