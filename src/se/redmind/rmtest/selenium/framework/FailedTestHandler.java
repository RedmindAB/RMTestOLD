package se.redmind.rmtest.selenium.framework;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class FailedTestHandler extends TestWatcher{
	
    @Override
    protected void failed(Throwable e, Description description) {
    	
        System.out.println("Onlyuted when a test fails. Failing Method: " + description.getClassName() + "." + description.getMethodName());
    }
}
