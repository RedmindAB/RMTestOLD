package se.redmind.rmtest.config;

import java.io.File;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import se.redmind.rmtest.WebDriverWrapper;
import se.redmind.utils.TestHome;

/**
 * @author Jeremy Comte
 */
@JsonTypeName("chrome")
public class ChromeConfiguration extends LocalConfiguration<ChromeDriver> {

    private static final String CHROMEDRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";

    @JsonProperty
    public String chromedriver;

    public ChromeConfiguration() {
        super(DesiredCapabilities.chrome(), capabilities -> new ChromeDriver(capabilities));
    }

    @Override
    protected List<WebDriverWrapper<ChromeDriver>> createDrivers() {
        if (System.getProperty(CHROMEDRIVER_SYSTEM_PROPERTY) == null) {
            System.setProperty(CHROMEDRIVER_SYSTEM_PROPERTY, getChromePath());
        }
        return super.createDrivers();
    }

    private String getChromePath() {
        String chromePath;
        if (chromedriver != null) {
            chromePath = chromedriver;
        } else {
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Mac") || osName.startsWith("Linux")) {
                chromePath = TestHome.get() + "/node_modules/chromedriver/bin/chromedriver";
            } else if (osName.startsWith("Windows")) {
                chromePath = TestHome.get() + "/windows/chromedriver.exe";
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
