package se.redmind.rmtest.config;

import se.redmind.rmtest.DriverWrapper;

import java.io.File;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import se.redmind.rmtest.selenium.framework.Browser;
import se.redmind.rmtest.selenium.grid.TestHome;

/**
 * @author Jeremy Comte
 */
@JsonTypeName("chrome")
public class ChromeConfiguration extends LocalConfiguration<ChromeDriver> {

    @JsonProperty
    public String chromedriver;

    public ChromeConfiguration() {
        super(DesiredCapabilities.chrome());
    }

    @Override
    protected DriverWrapper<ChromeDriver> createDriver() {
        System.setProperty("webdriver.chrome.driver", getChromePath());
        return new DriverWrapper<>(baseCapabilities, Browser.Chrome.toString(), (capabilities) -> new ChromeDriver(capabilities));
    }

    private String getChromePath() {
        String chromePath;
        if (chromedriver != null) {
            chromePath = chromedriver;
        } else {
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Mac")) {
                chromePath = TestHome.main() + "/node_modules/chromedriver/bin/chromedriver";
            } else if (osName.startsWith("Linux")) {
                chromePath = TestHome.main() + "/lib/linux/chromedriver";
            } else if (osName.startsWith("Windows")) {
                chromePath = TestHome.main() + "/windows/chromedriver.exe";
            } else {
                throw new RuntimeException("Unsupported platform: '" + osName + "'");
            }
        }

        if (!new File(chromePath).exists()) {
            throw new RuntimeException("chromedriver is not properly installed!");
        } else {
            logger.info("Setting chromedriver to be: " + chromePath);
        }
        return chromePath;
    }

}
