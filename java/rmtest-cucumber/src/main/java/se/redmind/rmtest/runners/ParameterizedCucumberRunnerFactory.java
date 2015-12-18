package se.redmind.rmtest.runners;

import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * @author Jeremy Comte
 */
public class ParameterizedCucumberRunnerFactory implements ParametersRunnerFactory {

    @Override
    public ParameterizedCucumber createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        return ParameterizedCucumber.create(test.getTestClass().getJavaClass(), test.getParameters().toArray());
    }

}
