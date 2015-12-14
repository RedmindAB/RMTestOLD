package se.redmind.rmtest.selenium;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.runners.DriverRunner;
import se.redmind.rmtest.runners.ReuseDriverBetweenTests;
import se.redmind.utils.Try;

@RunWith(DriverRunner.class)
//@Parallelize(tests = true, drivers = false)
@ReuseDriverBetweenTests
public class GoogleExample {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final DriverWrapper<?> wrapper;

    public GoogleExample(DriverWrapper<?> driverWrapper) {
        this.wrapper = driverWrapper;
    }

    @Test
    public void testGoogle() throws Exception {
        wrapper.getDriver().get("http://www.google.se");

        String pageTitle = Try.toGet(() -> wrapper.getDriver().getTitle())
            .until(value -> !Strings.isNullOrEmpty(value))
            .delayRetriesBy(500)
            .nTimes(10);

        logger.info("Page title is: " + pageTitle);

        assertTrue(pageTitle.startsWith("Goo"));

        logger.info(Thread.currentThread() + " is done!");
    }

    @Test
    public void testGoogle2() throws Exception {
        wrapper.getDriver().get("http://www.google.se");

        String pageTitle = Try.toGet(() -> wrapper.getDriver().getTitle())
            .until(value -> !Strings.isNullOrEmpty(value))
            .delayRetriesBy(500)
            .nTimes(10);

        logger.info("Page title is: " + pageTitle);

        assertTrue(pageTitle.startsWith("Goo"));

        logger.info(Thread.currentThread() + " is done!");
    }

}
