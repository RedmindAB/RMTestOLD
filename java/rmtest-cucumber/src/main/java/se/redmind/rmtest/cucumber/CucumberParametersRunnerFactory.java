package se.redmind.rmtest.cucumber;

import java.io.IOException;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import cucumber.api.junit.Cucumber;

/**
 * @author Jeremy Comte
 */
public class CucumberParametersRunnerFactory implements ParametersRunnerFactory {

    @Override
    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        try {
            return new Cucumber(test.getTestClass().getJavaClass());
        } catch (IOException ex) {
            throw new InitializationError(ex);
        }
    }

}
