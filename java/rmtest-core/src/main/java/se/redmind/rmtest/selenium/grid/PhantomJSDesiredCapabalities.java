package se.redmind.rmtest.selenium.grid;

import java.util.ArrayList;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by johgri on 15-09-29.
 */
public class PhantomJSDesiredCapabalities {
    public Capabilities createPhantomJSCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
        ArrayList<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--webdriver=6666");
        cliArgsCap.add("--ignore-ssl-errors=true");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--remote-debugger-autorun=yes");
        cliArgsCap.add("--remote-debugger-port=6666");
        caps.setCapability("takesScreenshot", true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/bin/phantomjs");
        caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36 s");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        return caps;
    }
}
