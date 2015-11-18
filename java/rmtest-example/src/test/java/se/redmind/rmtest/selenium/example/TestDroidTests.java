package se.redmind.rmtest.selenium.example;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.redmind.rmtest.selenium.example.testdroid.TestDroidScreenShotTest;
import se.redmind.rmtest.selenium.framework.RmSuite;

@RunWith(RmSuite.class)
@Suite.SuiteClasses({TestDroidScreenShotTest.class})
public class TestDroidTests {

}
