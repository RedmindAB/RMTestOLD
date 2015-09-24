package se.redmind.rmtest.selenium.framework.config;

import se.redmind.rmtest.selenium.grid.TestHome;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * This is a replacement for RmConfig, because that class is error-prone.
 */
public class FrameworkConfig {

    private final JsonObject localConfig;

    public static FrameworkConfig getConfig() {
        return new FrameworkConfig();
    }

    public FrameworkConfig(final String localConfigFile) {
        this.localConfig = parse(localConfigFile);
    }

    protected FrameworkConfig() {
        this(TestHome.main() + "/etc/LocalConfig.json");
    }

    private static JsonObject parse(final String configFile) {
        try {
            final String json = IOUtils.toString(new FileInputStream(configFile));
            return new Gson().fromJson(json, JsonObject.class);
        }
        catch(final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getLocalConfigValue(final String configKey) {
        return this.localConfig.getAsJsonObject("configuration").get(configKey).getAsString();
    }

    public boolean runOnGrid() {
        return "true".equalsIgnoreCase(this.getLocalConfigValue("runOnGrid"));
    }

    public boolean usePhantomJS() {
        return "true".equalsIgnoreCase(this.getLocalConfigValue("usePhantomJS"));
    }

    public boolean useFirefox() {
        return "true".equalsIgnoreCase(this.getLocalConfigValue("useFirefox"));
    }

    public boolean useChrome() {
        return "true".equalsIgnoreCase(this.getLocalConfigValue("useChrome"));
    }

    public boolean autoCloseDrivers() {
        return "true".equalsIgnoreCase(getLocalConfigValue("autoCloseDrivers"));
    }

    public String getRMRLiveAddress() {
        return Objects.firstNonNull(getLocalConfigValue("RmReportIP"), "127.0.0.1");
    }

    public Integer getRMRLivePort() {
        return Integer.parseInt(Objects.firstNonNull(getLocalConfigValue("RmReportLivePort"), "12345"));
    }

    public boolean enableLiveStream() {
        return "true".equalsIgnoreCase(getLocalConfigValue("enableLiveStream"));
    }

    public String getJsonReportSavePath() {
        return Objects.firstNonNull(getLocalConfigValue("jsonReportSavePath"), TestHome.main() + "/target/RMTReports");
    }

    public String getAndroidHome() {
        return this.getLocalConfigValue("androidHome");
    }

    public String getHubIp() {
        return this.getLocalConfigValue("hubIp");
    }

    public String getLocalIp() {
        return this.getLocalConfigValue("localIp");
    }

    public String getBuildToolsVersion() {
        return this.getLocalConfigValue("AndroidBuildtoolsVersion");
    }
}
