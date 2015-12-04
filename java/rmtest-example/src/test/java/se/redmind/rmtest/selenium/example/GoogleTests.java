package se.redmind.rmtest.selenium.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.redmind.rmtest.selenium.framework.RmSuite;

@RunWith(RmSuite.class)
@Suite.SuiteClasses({GoogleExample.class})
public class GoogleTests {

}
