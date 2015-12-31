package cucumber.runtime.java;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cucumber.api.java.ObjectFactory;
import cucumber.runtime.Runtime;
import cucumber.runtime.java.picocontainer.PicoFactory;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.model.TagStatement;
import javassist.*;
import se.redmind.rmtest.cucumber.utils.Tags;
import se.redmind.rmtest.runners.ParameterizedCucumber;
import se.redmind.utils.Fields;

/**
 * Cucumber java doesn't allow us to call Scenarios in a Scenario like in Ruby.
 *
 * This class is there to enable us to do that. If a Scenario is annotated with @parameterized we will create a dynamic JavaStepDefinition that will swallow the
 * call to the parameterized scenario. If the scenario is annotated with @quiet, it will internally trigger the sub steps, otherwise the real steps will be
 * added to the current feature in the ParameterizedCucumber class
 *
 * @see ParameterizedCucumber#getChildren()
 * @author Jeremy Comte
 */
public class ParameterizedJavaStepDefinition extends JavaStepDefinition {

    private final String[] parameters;

    public ParameterizedJavaStepDefinition(Method method, Pattern pattern, long timeoutMillis, ObjectFactory objectFactory, String[] parameters) {
        super(method, pattern, timeoutMillis, objectFactory);
        this.parameters = parameters;
    }

    public String[] parameters() {
        return parameters;
    }

    public static ParameterizedJavaStepDefinition from(CucumberTagStatement statement, JUnitReporter jUnitReporter, Runtime runtime) {
        boolean quiet = Tags.isQuiet(statement);
        PicoFactory objectFactory = ParameterizedCucumber.getPicoFactory(runtime);
        TagStatement tagStatement = Fields.getSafeValue(statement, "statement");
        String name = tagStatement.getName().replaceAll("(Given|When|Then|And)", "");

        // let's parse the regex and write down the parameters names
        StringBuilder patternBuilder = new StringBuilder();
        StringBuilder parameterBuilder = new StringBuilder();
        StringBuilder parametersNamesBuilder = new StringBuilder("new String[]{");
        List<String> parametersNames = new ArrayList<>();
        boolean inParameter = false;
        int parametersCount = 0;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            switch (c) {
                case '<':
                    if (inParameter) {
                        throw new IllegalArgumentException("error while parsing " + name);
                    }
                    inParameter = true;
                    parametersCount++;
                    patternBuilder.append("(.*)");
                    break;
                case '>':
                    if (!inParameter) {
                        throw new IllegalArgumentException("error while parsing " + name);
                    }
                    inParameter = false;
                    if (parametersNamesBuilder.length() > 13) {
                        parametersNamesBuilder.append(", ");
                    }
                    parametersNamesBuilder.append("\"").append(parameterBuilder).append("\"");
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
        
        Pattern pattern = Pattern.compile(patternBuilder.toString());
        parametersNamesBuilder.append("}");

        // make the method's parameters
        StringBuilder parametersBuilder = new StringBuilder();
        StringBuilder parametersListBuilder = new StringBuilder("new Object[]{");
        Class<?>[] parametersClasses = new Class<?>[parametersCount];

        for (int i = 0; i < parametersCount; i++) {
            if (parametersBuilder.length() > 0) {
                parametersBuilder.append(", ");
                parametersListBuilder.append(", ");
            }
            parametersBuilder.append("String arg").append(i);
            parametersListBuilder.append("arg").append(i);
            parametersClasses[i] = String.class;
        }

        parametersListBuilder.append("}");

        // and finally create the dynamic class and its method
        // if it's a quiet scenario we need to store the current tagStatement and runtime on the class itself
        // as the only thing that will get passed down is the method.
        Method method = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass("cucumber.runtime.model.ParameterizedScenario@" + Integer.toHexString(statement.hashCode()));

            String methodSource;

            if (quiet) {
                ctClass.addField(CtField.make("public static cucumber.runtime.model.StepContainer STEPCONTAINER;", ctClass));
                ctClass.addField(CtField.make("public static cucumber.runtime.Runtime RUNTIME;", ctClass));

                methodSource = ""
                    + "public void accept(" + parametersBuilder.toString() + ") {\n"
                    + "     cucumber.runtime.java.QuietReporter reporter = new cucumber.runtime.java.QuietReporter();"
                    + "     cucumber.runtime.model.ParameterizedStepContainer parameterizedStepContainer = "
                    + "new cucumber.runtime.model.ParameterizedStepContainer(STEPCONTAINER, " + parametersNamesBuilder.toString() + ", " + parametersListBuilder.toString() + ");\n"
                    + "     se.redmind.utils.Methods.invoke(parameterizedStepContainer, \"format\", new Object[] { reporter });\n"
                    + "     se.redmind.utils.Methods.invoke(parameterizedStepContainer, \"runSteps\", new Object[] { reporter, RUNTIME });\n"
                    + "}";
            } else {
                methodSource = "public void accept(" + parametersBuilder.toString() + ") {}";
            }

            ctClass.addMethod(CtNewMethod.make(methodSource, ctClass));
            Class<?> clazz = ctClass.toClass();

            if (quiet) {
                clazz.getDeclaredField("STEPCONTAINER").set(null, statement);
                clazz.getDeclaredField("RUNTIME").set(null, runtime);
            }

            method = clazz.getMethod("accept", parametersClasses);
            objectFactory.addClass(method.getDeclaringClass());
        } catch (CannotCompileException | NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        return new ParameterizedJavaStepDefinition(method, pattern, 0, objectFactory, parametersNames.toArray(new String[0]));
    }

}
