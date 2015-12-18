package cucumber.runtime.java;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import cucumber.api.java.ObjectFactory;
import cucumber.runtime.Runtime;
import cucumber.runtime.java.picocontainer.PicoFactory;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.model.TagStatement;
import javassist.*;
import se.redmind.rmtest.runners.ParameterizedCucumber;
import se.redmind.utils.Fields;

/**
 * Cucumber java doesn't allow us to call Scenarios in a Scenario like in Ruby.
 *
 * This class is there to enable us to do that. if a Scenario is annotated with @parameterized we will create a dynamic JavaStepDefinition that will encapsulate
 * the scenario and run it when a specific step definition is called from within another scenario.
 *
 * @author Jeremy Comte
 */
public class ParameterizedJavaStepDefinition extends JavaStepDefinition {

    public ParameterizedJavaStepDefinition(Method method, Pattern pattern, long timeoutMillis, ObjectFactory objectFactory) {
        super(method, pattern, timeoutMillis, objectFactory);
    }

    public static ParameterizedJavaStepDefinition from(CucumberTagStatement statement, JUnitReporter jUnitReporter, Runtime runtime) {
        PicoFactory objectFactory = ParameterizedCucumber.getPicoFactory(runtime);
        TagStatement tagStatement = Fields.getSafeValue(statement, "statement");
        String name = tagStatement.getName().replaceAll("(?:\\w+) (.*)", "$1");
        // let's parse the regex and write down the parameters names
        StringBuilder patternBuilder = new StringBuilder();
        StringBuilder parameterBuilder = new StringBuilder();
        StringBuilder parametersNamesBuilder = new StringBuilder("new String[]{");
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
            if (parametersBuilder.length() > 13) {
                parametersBuilder.append(", ");
                parametersListBuilder.append(", ");
            }
            parametersBuilder.append("String arg").append(i);
            parametersListBuilder.append("arg").append(i);
            parametersClasses[i] = String.class;
        }

        parametersListBuilder.append("}");

        // and finally create the dynamic class and its method
        // we need to store the current tagStatement and reporter on the class itself as the only thing that
        // will get passed down is the method.
        Method method = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass("cucumber.runtime.model.ParameterizedScenario$" + statement.toString());

            ctClass.addField(CtField.make("public static cucumber.runtime.model.StepContainer STEPCONTAINER;", ctClass));
            ctClass.addField(CtField.make("public static cucumber.runtime.java.WrapperReporter REPORTER;", ctClass));
            ctClass.addField(CtField.make("public static cucumber.runtime.Runtime RUNTIME;", ctClass));

            CtMethod ctMethod = CtNewMethod.make(""
                + "public void accept(" + parametersBuilder.toString() + ") {\n"
                + "     cucumber.runtime.model.ParameterizedStepContainer parameterizedStepContainer = "
                + "         new cucumber.runtime.model.ParameterizedStepContainer(STEPCONTAINER, " + parametersNamesBuilder.toString() + ", " + parametersListBuilder.toString() + ");"
                + "     se.redmind.utils.Methods.invoke(parameterizedStepContainer, \"format\", new Object[] { REPORTER });\n"
                + "     se.redmind.utils.Methods.invoke(parameterizedStepContainer, \"runSteps\", new Object[] { REPORTER, RUNTIME });\n"
                + "}",
                ctClass);

            ctClass.addMethod(ctMethod);
            Class<?> clazz = ctClass.toClass();
            clazz.getDeclaredField("STEPCONTAINER").set(null, statement);
            clazz.getDeclaredField("REPORTER").set(null, new WrapperReporter(jUnitReporter));
            clazz.getDeclaredField("RUNTIME").set(null, runtime);
            method = clazz.getMethod("accept", parametersClasses);
            objectFactory.addClass(method.getDeclaringClass());
        } catch (CannotCompileException | NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        return new ParameterizedJavaStepDefinition(method, pattern, 0, objectFactory);
    }
}
