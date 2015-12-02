package se.redmind.rmtest.cucumber;

import org.junit.runners.Parameterized;


/**
 * @author Jeremy Comte
 */
public class ParameterizedCucumber extends Parameterized {


    public ParameterizedCucumber(Class<?> klass) throws Throwable {
        super(klass);
    }

}
