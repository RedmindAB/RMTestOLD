package se.redmind.rmtest.config;

import se.redmind.rmtest.DriverWrapper;

import java.util.ArrayList;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import se.redmind.rmtest.selenium.framework.Browser;
import se.redmind.rmtest.selenium.grid.TestHome;

/**
 * @author Jeremy Comte
 */
@JsonTypeName("phantomjs")
public class PhantomJSConfiguration extends LocalConfiguration<PhantomJSDriver> {

    public PhantomJSConfiguration() {
        super(createPhantomJSCapabilities());
    }

    @Override
    protected DriverWrapper<PhantomJSDriver> createDriver() {
        return new DriverWrapper<>(baseCapabilities, Browser.PhantomJS.toString(), (capabilities) -> new PhantomJSDriver(capabilities));
    }

    public static DesiredCapabilities createPhantomJSCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        ArrayList<String> cliArgs = new ArrayList<>();
        cliArgs.add("--webdriver=6666");
        cliArgs.add("--ignore-ssl-errors=true");
        cliArgs.add("--ssl-protocol=any");
        cliArgs.add("--remote-debugger-autorun=yes");
        cliArgs.add("--remote-debugger-port=6666");
        capabilities.setCapability("takesScreenshot", true);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, TestHome.main() + "/node_modules/phantomjs/bin/phantomjs");
        capabilities.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36 s");
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgs);
        return capabilities;
    }
}
