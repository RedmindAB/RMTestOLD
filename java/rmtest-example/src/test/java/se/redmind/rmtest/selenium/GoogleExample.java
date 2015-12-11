package se.redmind.rmtest.selenium;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Strings;
import se.redmind.utils.Try;

import se.redmind.rmtest.DriverWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redmind.rmtest.runners.Parallelize;
import se.redmind.rmtest.runners.DriverRunner;

import static org.junit.Assert.assertTrue;

@RunWith(DriverRunner.class)
@Parallelize
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

        logger.info("Done!");
    }

}
