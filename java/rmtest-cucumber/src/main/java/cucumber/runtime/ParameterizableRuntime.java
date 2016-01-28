package cucumber.runtime;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.StepDefinitionReporter;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.java.ParameterizedJavaStepDefinition;
import cucumber.runtime.java.picocontainer.PicoFactory;
import cucumber.runtime.model.*;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;
import gherkin.formatter.model.TagStatement;
import se.redmind.rmtest.cucumber.utils.Tags;
import se.redmind.utils.Fields;
import se.redmind.utils.Methods;

/**
 * @author Jeremy Comte
 */
public class ParameterizableRuntime extends Runtime {

    private static enum CompositionType {

        Replace, Full, Quiet
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RuntimeOptions runtimeOptions;
    private final ClassLoader classLoader;
    private final ResourceLoader resourceLoader;
    private PicoFactory picoFactory;
    private String name;
    private Object[] parameters;

    public ParameterizableRuntime(ResourceLoader resourceLoader, ClassFinder classFinder, ClassLoader classLoader, RuntimeOptions runtimeOptions) {
        super(resourceLoader, classFinder, classLoader, runtimeOptions);
        this.runtimeOptions = runtimeOptions;
        this.classLoader = classLoader;
        this.resourceLoader = resourceLoader;
    }

    public ParameterizableRuntime(ResourceLoader resourceLoader, ClassFinder classFinder, ClassLoader classLoader, RuntimeOptions runtimeOptions, String name, Object[] parameters) {
        this(resourceLoader, classFinder, classLoader, runtimeOptions);
        this.name = name;
        this.parameters = parameters;
    }

    @Override
    public void run() throws IOException {
        // Make sure all features parse before initialising any reporters/formatters
        List<CucumberFeature> features = cucumberFeatures();

        try (Formatter formatter = runtimeOptions.formatter(classLoader)) {
            Reporter reporter = runtimeOptions.reporter(classLoader);
            StepDefinitionReporter stepDefinitionReporter = runtimeOptions.stepDefinitionReporter(classLoader);
            getGlue().reportStepDefinitions(stepDefinitionReporter);
            features.forEach(cucumberFeature -> cucumberFeature.run(formatter, reporter, this));
            formatter.done();
        }
        printSummary();
    }

    public List<CucumberFeature> cucumberFeatures() {
        List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);

        // 1. Get the children from the parent class, intercept any parameterized scenario and instantiate their factories
        Map<Pattern, ParameterizedJavaStepDefinition.Factory> parameterizedScenarios = getParameterizedScenarios(cucumberFeatures);

        // 2. Iterate over all the normal steps, and if the scenario is not quiet, rewrite and add the parameterized steps as normal steps.
        if (!parameterizedScenarios.isEmpty()) {
            inject(parameterizedScenarios, cucumberFeatures);
        }
        return cucumberFeatures;
    }

    @Override
    public void buildBackendWorlds(Reporter reporter, Set<Tag> tags, Scenario gherkinScenario) {
        for (Object parameter : parameters) {
            picoFactory().addInstance(parameter);
        }
        super.buildBackendWorlds(reporter, tags, gherkinScenario);
    }

    public Map<Pattern, ParameterizedJavaStepDefinition.Factory> getParameterizedScenarios(List<CucumberFeature> features) {
        Map<Pattern, ParameterizedJavaStepDefinition.Factory> parameterizedScenarios = new LinkedHashMap<>();
        features.forEach(feature -> {
            List<CucumberTagStatement> statements = feature.getFeatureElements();
            for (int i = 0; i < statements.size(); i++) {
                CucumberTagStatement statement = statements.get(i);
                if (Tags.isParameterized(statement)) {
                    ParameterizedJavaStepDefinition.Factory stepFactory = ParameterizedJavaStepDefinition.from(statement, this);
                    parameterizedScenarios.put(stepFactory.pattern(), stepFactory);
                    statements.remove(i);
                    i--;
                } else if (name != null) {
                    TagStatement tagStatement = statement.getGherkinModel();
                    Fields.set(tagStatement, "name", tagStatement.getName() + " " + name);
                }
            }
        });
        if (!parameterizedScenarios.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            int maxLength = parameterizedScenarios.values().stream().map(f -> f.statement().getVisualName()).max(String::compareTo).get().length();
            parameterizedScenarios.forEach((pattern, factory) -> {
                CucumberFeature cucumberFeature = Fields.getSafeValue(factory.statement(), "cucumberFeature");
                String path = Fields.getSafeValue(cucumberFeature, "path");
                String visualName = factory.statement().getVisualName().replaceAll("Scenario:", "");
                stringBuilder.append("\n  ").append(visualName);
                for (int i = 0; i < maxLength - visualName.length() - 5; i++) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append("# ").append(path).append(":").append(factory.statement().getGherkinModel().getLine());
            });
            logger.info("\nregistering parameterized scenarios:" + stringBuilder.toString() + "\n");
        }
        return parameterizedScenarios;
    }

    public void inject(Map<Pattern, ParameterizedJavaStepDefinition.Factory> parameterizedScenarios, List<CucumberFeature> features) throws RuntimeException {
        picoFactory().addInstance(this);
        getGlue().addStepDefinition(new ParameterizedJavaStepDefinition(Methods.findMethod(this.getClass(), "endOfParameterizedScenario"), Pattern.compile("}"), 0, picoFactory()));
        features.forEach(feature -> {
            List<StepContainer> stepContainers = new ArrayList<>(feature.getFeatureElements());

            CucumberBackground cucumberBackground = Fields.getSafeValue(feature, "cucumberBackground");
            if (cucumberBackground != null) {
                stepContainers.add(cucumberBackground);
            }

            parameterizedScenarios.values().stream().forEach(scenario -> stepContainers.add(scenario.statement()));

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
                        CompositionType compositionType = CompositionType.Replace;
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
                                break;
                            }
                        }
                    }
                }
            } while (modifiedSteps > 0);
        });
    }

    /**
     * this method is used as a target for the end of a parameterized scenario
     */
    public void endOfParameterizedScenario() {
    }

    public PicoFactory picoFactory() throws RuntimeException {
        if (picoFactory == null) {
            Collection<? extends Backend> backends = Fields.getSafeValue(this, "backends");
            Optional<JavaBackend> first = backends.stream()
                .filter(backend -> backend instanceof JavaBackend)
                .map(backend -> (JavaBackend) backend)
                .findFirst();
            if (first.isPresent()) {
                picoFactory = Fields.getSafeValue(first.get(), "objectFactory");
            } else {
                throw new RuntimeException("can't find a javaBackend instance");
            }
        }
        return picoFactory;
    }

}
