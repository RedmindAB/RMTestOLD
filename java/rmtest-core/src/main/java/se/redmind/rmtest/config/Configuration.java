package se.redmind.rmtest.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Set;

import javax.validation.*;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Iterators;
import com.google.common.collect.Table;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import se.redmind.rmtest.selenium.grid.TestHome;
import se.redmind.utils.Fields;

/**
 * @author Jeremy Comte
 */
public class Configuration {

    private static final String CONFIG_SYSTEM_PROPERTY = "rmtestConfig";
    private static final String DEFAULT_REPORTS_PATH = "/target/RMTReports";
    private static final String DEFAULT_LOCAL_CONFIG = "/etc/LocalConfig.yml";
    private static final String DEFAULT_LEGACY_CONFIG = "/etc/LocalConfig.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
    private static ObjectMapper objectMapper;
    private static Validator validator;

    // let's save the latest read config as a singleton, to be able to replicate the behavior of the legacy FrameworkConfig
    // TODO: dependency injection?
    private static Configuration current;

    @JsonIgnore
    private String filePath;

    @JsonProperty
    @NotNull
    @Valid
    public RunnerConfiguration runner;

    @JsonProperty
    public boolean autoCloseDrivers = true;

    @JsonProperty()
    public String rmReportIP = "127.0.0.1";

    @JsonProperty()
    public int rmReportLivePort = 12345;

    @JsonProperty
    public String jsonReportSavePath = TestHome.main() + DEFAULT_REPORTS_PATH;

    /**
     * @return the path of the file this configuration is based on
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Overrides configuration properties with applicable system properties
     *
     * @return the current configuration
     */
    public Configuration applySystemProperties() {
        Table<String, Object, Field> fieldsByPathAndDeclaringInstance = Fields.listByPathAndDeclaringInstance(this);
        fieldsByPathAndDeclaringInstance.cellSet().forEach(cell -> {
            String value = System.getProperty(cell.getRowKey());
            if (value != null) {
                if(value.contains("\\n")) {
                    value = value.replaceAll("\\\\n", "\n");
                }
                LOGGER.info("overriding configuration key '" + cell.getRowKey() + "' with '" + value + "'");
                try {
                    Field field = cell.getValue();
                    field.set(cell.getColumnKey(), objectMapper().readValue(value, field.getType()));
                } catch (IOException | IllegalArgumentException | IllegalAccessException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        });
        return this;
    }

    /**
     * Validates the configuration
     *
     * @return the current configuration
     */
    public Configuration validate() {
        Set<ConstraintViolation<Configuration>> violations = validator().validate(this);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder(violations.size() + " error" + (violations.size() > 1 ? "s" : "") + " in configuration file " + filePath);
            violations.forEach(violation -> {
                Path.Node node = Iterators.getLast(violation.getPropertyPath().iterator());
                try {
                    Class<?> type = violation.getLeafBean().getClass().getField(node.getName()).getType();
                    message.append("\n").append(violation.getPropertyPath()).append(" of type ").append(type.getName()).append(" ").append(violation.getMessage());
                } catch (NoSuchFieldException | SecurityException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            });
            throw new ValidationException(message.toString());
        }
        return this;
    }

    @Override
    public String toString() {
        try {
            return "# " + filePath + "\n" + objectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return super.toString();
    }

    /**
     * @return the last read configuration, if none was read then try to read the default one, then the legacy one
     */
    public static Configuration current() {
        if (current == null) {
            Configuration configuration;
            String configFile = null;
            try {
                configFile = System.getProperty(CONFIG_SYSTEM_PROPERTY);
                if (configFile == null) {
                    configFile = TestHome.main() + DEFAULT_LOCAL_CONFIG;
                }
                configuration = read(configFile);
            } catch (IOException e) {
                LOGGER.warn("cannot read " + configFile + ", trying legacy config " + DEFAULT_LEGACY_CONFIG);
                try {
                    configuration = read(TestHome.main() + DEFAULT_LEGACY_CONFIG);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            current = configuration.applySystemProperties().validate();
        }
        return current;
    }

    /**
     * read the file located at filepath
     *
     * @param filepath
     * @return the Configuration
     * @throws IOException
     */
    public static Configuration read(String filepath) throws IOException {
        return read(new File(filepath));
    }

    /**
     * read the file
     *
     * @param file
     * @return the Configuration
     * @throws IOException
     */
    public static Configuration read(File file) throws IOException {
        String content = Files.toString(file, Charset.defaultCharset()).trim();
        Configuration configuration;
        if (content.startsWith("{")) {
            configuration = fromLegacyJson(content);
        } else {
            configuration = fromYaml(content);
        }
        configuration.filePath = file.getAbsolutePath();
        return configuration;
    }

    /**
     * Builds a configuration object from a YAML String
     *
     * @param yaml
     * @return
     */
    public static Configuration fromYaml(String yaml) {
        try {
            return objectMapper().readValue(yaml, Configuration.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Builds a configuration object from a legacy JSON String
     *
     * @param json
     * @return
     */
    public static Configuration fromLegacyJson(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        if (jsonObject.has("configuration")) {
            JsonObject jsonConfiguration = jsonObject.getAsJsonObject("configuration");
            Configuration configuration = new Configuration();
            if (jsonConfiguration.has("runOnGrid") && jsonConfiguration.get("runOnGrid").getAsBoolean()) {
                GridConfiguration gridConfiguration = new GridConfiguration();
                if (jsonConfiguration.has("localIp")) {
                    gridConfiguration.localIp = jsonConfiguration.get("localIp").getAsString();
                }
                if (jsonConfiguration.has("hubIp")) {
                    gridConfiguration.hubIp = jsonConfiguration.get("hubIp").getAsString();
                }
                if (jsonConfiguration.has("enableLiveStream")) {
                    gridConfiguration.enableLiveStream = jsonConfiguration.get("enableLiveStream").getAsBoolean();
                }
            } else {
                LocalConfiguration localConfiguration = new LocalConfiguration();
                if (jsonConfiguration.has("usePhantomJS")) {
                    localConfiguration.usePhantomJS = jsonConfiguration.get("usePhantomJS").getAsBoolean();
                }
                if (jsonConfiguration.has("useFirefox")) {
                    localConfiguration.useFirefox = jsonConfiguration.get("useFirefox").getAsBoolean();
                }
                if (jsonConfiguration.has("useChrome")) {
                    localConfiguration.useChrome = jsonConfiguration.get("useChrome").getAsBoolean();
                }
                if (jsonConfiguration.has("androidHome")) {
                    localConfiguration.android = new AndroidConfiguration();
                    localConfiguration.android.home = jsonConfiguration.get("androidHome").getAsString();
                    if (jsonConfiguration.has("AndroidBuildtoolsVersion")) {
                        localConfiguration.android.toolsVersion = jsonConfiguration.get("AndroidBuildtoolsVersion").getAsFloat();
                    }
                }
                configuration.runner = localConfiguration;
            }
            if (jsonConfiguration.has("autoCloseDrivers")) {
                configuration.autoCloseDrivers = jsonConfiguration.get("autoCloseDrivers").getAsBoolean();
            }
            if (jsonConfiguration.has("RmReportIP")) {
                configuration.rmReportIP = jsonConfiguration.get("RmReportIP").getAsString();
            }
            if (jsonConfiguration.has("RmReportLivePort")) {
                configuration.rmReportLivePort = jsonConfiguration.get("RmReportLivePort").getAsInt();
            }
            if (jsonConfiguration.has("jsonReportSavePath")) {
                configuration.jsonReportSavePath = jsonConfiguration.get("jsonReportSavePath").getAsString();
            }
            return configuration;
        }
        throw new RuntimeException("config doesn't contain any 'configuration' object\ncontent:\n" + json);
    }

    /**
     * singleton of the YAML ObjectMapper
     *
     * @return the objectMapper
     */
    public static synchronized ObjectMapper objectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.registerSubtypes(LocalConfiguration.class, GridConfiguration.class);
        }
        return objectMapper;
    }

    /**
     * singleton of the Validator
     *
     * @return the validator
     */
    public static synchronized Validator validator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }
}
