package se.redmind.rmtest.selenium.grid;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by johgri on 15-09-29.
 */
public class PhantomJSDesiredCapabalities{

    public Capabilities createPhantomJSCapabilities(){
        DesiredCapabilities caps = new DesiredCapabilities();
        ArrayList<String> cliArgsCap = new ArrayList<String>();

        caps.setCapability("phantomjs.binary.path", "/usr/local/bin/phantomjs");
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        caps.setCapability("takesScreenshot", true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--remote-debugger-port=9000"} );
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--logLevel=DEBUG"});

        return caps;
    }

}
