package se.redmind.rmtest.config;

import java.util.ArrayList;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import se.redmind.utils.TestHome;

/**
 * @author Jeremy Comte
 */
@JsonTypeName("phantomjs")
public class PhantomJSConfiguration extends LocalConfiguration<PhantomJSDriver> {

    public PhantomJSConfiguration() {
        super(createPhantomJSCapabilities(), capabilities -> new PhantomJSDriver(capabilities));
    }

    public static DesiredCapabilities createPhantomJSCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        ArrayList<String> cliArgs = new ArrayList<>();
        cliArgs.add("--ignore-ssl-errors=true");
        cliArgs.add("--ssl-protocol=any");
        capabilities.setCapability("takesScreenshot", true);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, TestHome.get() + "/node_modules/phantomjs/bin/phantomjs");
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", true);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptEnabled", true);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36 s");
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgs);
        return capabilities;
    }
}
