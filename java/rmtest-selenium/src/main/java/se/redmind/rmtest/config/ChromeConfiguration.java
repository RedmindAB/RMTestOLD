package se.redmind.rmtest.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import se.redmind.rmtest.WebDriverWrapper;
import se.redmind.utils.NodeModules;
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
        if (System.getProperty(CHROMEDRIVER_SYSTEM_PROPERTY) == null && !setChromePath()) {
            return new ArrayList<>();
        }
        return super.createDrivers();
    }

    private boolean setChromePath() {
        String chromePath = null;
        String osName = System.getProperty("os.name");
        if (chromedriver != null) {
            chromePath = chromedriver;
        } else {
            if (osName.startsWith("Mac") || osName.startsWith("Linux")) {
                chromePath = NodeModules.path() + "/chromedriver/lib/chromedriver/chromedriver";
            } else if (osName.startsWith("Windows")) {
                chromePath = TestHome.get() + "/windows/chromedriver.exe";
            } else {
                throw new RuntimeException("Unsupported platform: '" + osName + "'");
            }
        }

        if (!new File(chromePath).exists()) {
            logger.error("you need to specify the location of the chromedriver, either by:\n"
                + "\t- setting the chromedriver property on the current driver in your configuration file\n"
                + "\t- setting the webdriver.chrome.driver system property (with -Dwebdriver.chrome.driver=/path/to/your/chromedriver)\n"
                + "\t- running npm install chromedriver in the current folder (" + System.getProperty("user.dir") + "), one of its parent folder"
                + (TestHome.get() != null ? " or the TestHome folder (" + TestHome.get() + ")" : ""));
            return false;
        } else {
            logger.info("Setting chromedriver to be: " + chromePath);
            System.setProperty(CHROMEDRIVER_SYSTEM_PROPERTY, chromePath);
            return true;
        }
    }
}
