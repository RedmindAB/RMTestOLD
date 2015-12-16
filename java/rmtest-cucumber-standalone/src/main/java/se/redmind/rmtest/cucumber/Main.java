package se.redmind.rmtest.cucumber;

import org.junit.experimental.results.PrintableResult;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.CucumberOptions;
import se.redmind.rmtest.runners.WebDriverRunner;
import se.redmind.utils.LogBackUtil;

/**
 * @author Jeremy Comte
 */
public class Main {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        LogBackUtil.install();
        new Main().run();
    }

    public void run() {
        Result result = new JUnitCore().run(Test.class);
        if (result.getFailureCount() > 0) {
            logger.error(new PrintableResult(result.getFailures()).toString());
        }
        logger.info(String.format("Tests run: %d, Failures: %d, Skipped: %d, Time elapsed: %.4f sec",
            result.getRunCount(), result.getFailureCount(), result.getIgnoreCount(), result.getRunTime() / 1_000.0));
    }

    @RunWith(WebDriverRunner.class)
    @Parameterized.UseParametersRunnerFactory(CucumberParametersRunnerFactory.class)
    @CucumberOptions(glue = "se.redmind.rmtest.cucumber.web", features = ".", plugin = "pretty")
    public static class Test {

    }

}
