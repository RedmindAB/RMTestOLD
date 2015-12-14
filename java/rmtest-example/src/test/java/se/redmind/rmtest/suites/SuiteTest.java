package se.redmind.rmtest.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import se.redmind.rmtest.runners.Parallelize;
import se.redmind.rmtest.selenium.grid.Annotations.MultiThreadedSuite;

/**
 * @author Jeremy Comte
 */
@RunWith(MultiThreadedSuite.class)
@Parallelize(threads = 8)
@Suite.SuiteClasses({
    se.redmind.rmtest.cucumber.GoogleExample.class,
    se.redmind.rmtest.selenium.GoogleExample.class,
    se.redmind.rmtest.cucumber.GoogleExample.class,
    se.redmind.rmtest.selenium.GoogleExample.class,
    se.redmind.rmtest.cucumber.GoogleExample.class,
    se.redmind.rmtest.selenium.GoogleExample.class,
    se.redmind.rmtest.cucumber.GoogleExample.class,
    se.redmind.rmtest.selenium.GoogleExample.class
})
public class SuiteTest {

}
