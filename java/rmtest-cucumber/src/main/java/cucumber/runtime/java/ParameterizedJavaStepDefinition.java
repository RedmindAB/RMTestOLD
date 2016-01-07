package cucumber.runtime.java;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cucumber.api.java.ObjectFactory;
import cucumber.api.junit.Cucumber;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeGlue;
import cucumber.runtime.java.picocontainer.PicoFactory;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.model.TagStatement;
import javassist.*;
import se.redmind.rmtest.cucumber.web.WebDriverSteps;
import se.redmind.utils.Fields;
import se.redmind.utils.Methods;

/**
 * Cucumber java doesn't allow us to call Scenarios in a Scenario like in Ruby.
 *
 * This class is here to enable us to do that. If a Scenario is annotated with @parameterized we will create a dynamic JavaStepDefinition that will be
 * registered on the pattern made by the name of this scenario.
 * Depending on how this scenario is called, the real steps will be added to the current feature in the ParameterizedCucumber class or executed silently.
 *
 * @see Cucumber#addChildren(java.util.List)
 * @author Jeremy Comte
 */
public class ParameterizedJavaStepDefinition extends JavaStepDefinition {

    private static final String PARAMETER_PATTERN = "([0-9]+|[0-9]*\\.[0-9]+|\".*\"|<.*>)";

    public ParameterizedJavaStepDefinition(Method method, Pattern pattern, long timeoutMillis, ObjectFactory objectFactory) {
        super(method, pattern, timeoutMillis, objectFactory);
    }

    public static ParameterizedJavaStepDefinition.Factory from(CucumberTagStatement statement, JUnitReporter jUnitReporter, Runtime runtime) {
        TagStatement tagStatement = Fields.getSafeValue(statement, "statement");
        String name = tagStatement.getName().replaceAll("(Given|When|Then|And)", "");

        // let's parse the regex and write down the parameters names
        StringBuilder patternBuilder = new StringBuilder("^" + WebDriverSteps.THAT);
        StringBuilder parameterBuilder = new StringBuilder();
        List<String> parametersNames = new ArrayList<>();
        boolean inParameter = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            switch (c) {
                case '<':
                    if (inParameter) {
                        throw new IllegalArgumentException("error while parsing " + name);
                    }
                    inParameter = true;
                    patternBuilder.append(PARAMETER_PATTERN);
                    break;
                case '>':
                    if (!inParameter) {
                        throw new IllegalArgumentException("error while parsing " + name);
                    }
                    inParameter = false;
                    parametersNames.add(parameterBuilder.toString());
                    parameterBuilder = new StringBuilder();
                    break;
                default:
                    if (!inParameter) {
                        patternBuilder.append(c);
                    } else {
                        parameterBuilder.append(c);
                    }
                    break;
            }
        }

        // No need to instanciate everything if no scenario is using it, so we'll create the dynamic class and its step definitions on demand.
        return new Factory(statement, Pattern.compile(patternBuilder.append("$").toString()), parametersNames.toArray(new String[0]), runtime);
    }

    public static class Factory {

        private final CucumberTagStatement statement;
        private final Pattern pattern;
        private final String[] parameters;
        private final Runtime runtime;
        private final RuntimeGlue glue;
        private final PicoFactory objectFactory;

        private Class<?> clazz;
        private Class<?>[] parametersClasses;
        private ParameterizedJavaStepDefinition subSteps;
        private ParameterizedJavaStepDefinition start;

        public Factory(CucumberTagStatement statement, Pattern pattern, String[] parameters, Runtime runtime) {
            this.statement = statement;
            this.pattern = pattern;
            this.parameters = parameters;
            this.runtime = runtime;
            this.objectFactory = Cucumber.getPicoFactory(runtime);
            this.glue = (RuntimeGlue) runtime.getGlue();
        }

        public CucumberTagStatement statement() {
            return statement;
        }

        public Pattern pattern() {
            return pattern;
        }

        public String[] parameters() {
            return parameters;
        }

        private synchronized Class<?> clazz() {
            // and finally create the dynamic class and its methods
            // we need to store the current tagStatement and runtime on the class itself
            // as the only thing that will get passed down is the method.
            if (clazz == null) {
                try {
                    // make the methods' parameters
                    StringBuilder parametersNamesBuilder = new StringBuilder("new String[]{");
                    for (String parameter : parameters) {
                        if (parametersNamesBuilder.length() > 13) {
                            parametersNamesBuilder.append(", ");
                        }
                        parametersNamesBuilder.append("\"").append(parameter).append("\"");
                    }
                    parametersNamesBuilder.append("}");

                    StringBuilder parametersBuilder = new StringBuilder();
                    StringBuilder parametersListBuilder = new StringBuilder("new Object[]{");
                    parametersClasses = new Class<?>[parameters.length];

                    for (int i = 0; i < parameters.length; i++) {
                        if (parametersBuilder.length() > 0) {
                            parametersBuilder.append(", ");
                            parametersListBuilder.append(", ");
                        }
                        parametersBuilder.append("String arg").append(i);
                        parametersListBuilder.append("arg").append(i);
                        parametersClasses[i] = String.class;
                    }

                    parametersListBuilder.append("}");

                    ClassPool pool = ClassPool.getDefault();
                    CtClass ctClass = pool.makeClass("cucumber.runtime.model.ParameterizedScenario@" + Integer.toHexString(statement.hashCode()));

                    ctClass.addField(CtField.make("public static cucumber.runtime.model.StepContainer STEPCONTAINER;", ctClass));
                    ctClass.addField(CtField.make("public static cucumber.runtime.Runtime RUNTIME;", ctClass));

                    ctClass.addMethod(CtNewMethod.make(""
                        + "public void execute(" + parametersBuilder.toString() + ") {\n"
                        + "     cucumber.runtime.java.QuietReporter reporter = new cucumber.runtime.java.QuietReporter();"
                        + "     cucumber.runtime.model.ParameterizedStepContainer parameterizedStepContainer = "
                        + "new cucumber.runtime.model.ParameterizedStepContainer(STEPCONTAINER, " + parametersNamesBuilder.toString() + ", " + parametersListBuilder.toString() + ");\n"
                        + "     se.redmind.utils.Methods.invoke(parameterizedStepContainer, \"format\", new Object[] { reporter });\n"
                        + "     se.redmind.utils.Methods.invoke(parameterizedStepContainer, \"runSteps\", new Object[] { reporter, RUNTIME });\n"
                        + "}", ctClass));

                    ctClass.addMethod(CtNewMethod.make("public void start(" + parametersBuilder.toString() + ") {}", ctClass));

                    clazz = ctClass.toClass();

                    clazz.getDeclaredField("STEPCONTAINER").set(null, statement);
                    clazz.getDeclaredField("RUNTIME").set(null, runtime);
                    objectFactory.addClass(clazz);
                } catch (CannotCompileException | SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return clazz;
        }

        public synchronized ParameterizedJavaStepDefinition addQuietSubStepsToGlue() {
            if (subSteps == null) {
                subSteps = new ParameterizedJavaStepDefinition(Methods.findMethod(clazz(), "execute", parametersClasses), pattern, 0, objectFactory);
                glue.addStepDefinition(subSteps);
            }
            return subSteps;
        }

        public synchronized ParameterizedJavaStepDefinition addStartStepToGlue() {
            if (start == null) {
                start = new ParameterizedJavaStepDefinition(Methods.findMethod(clazz(), "start", parametersClasses),
                    Pattern.compile(pattern.pattern().substring(0, pattern.pattern().length() - 1) + "(?: \\{)$"), 0, objectFactory);
                glue.addStepDefinition(start);
            }
            return start;

        }
    }

}
