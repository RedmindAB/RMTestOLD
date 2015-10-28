package se.redmind.rmtest.config;

import java.io.IOException;

import javax.validation.ValidationException;

import org.junit.*;

/**
 * @author Jeremy Comte
 */
public class ConfigurationTest {

    @Test(expected = ValidationException.class)
    public void readAndValidateInvalidConfig() throws IOException {
        Configuration configuration = Configuration.read("src/test/resources/se/redmind/rmtest/config/invalidConfig.yml");
        Assert.assertNotNull(configuration);
        configuration.validate();
    }

    @Test
    public void readAndValidateLocalConfig() throws IOException {
        Configuration configuration = Configuration.read("src/test/resources/se/redmind/rmtest/config/localConfig.yml");
        Assert.assertNotNull(configuration);
        configuration.validate();
    }

    @Test
    public void readAndValidateGridConfig() throws IOException {
        Configuration configuration = Configuration.read("src/test/resources/se/redmind/rmtest/config/gridConfig.yml");
        Assert.assertNotNull(configuration);
        configuration.validate();
    }

    @Test
    public void readLegacy() throws IOException {
        Configuration configuration = Configuration.read("src/test/resources/se/redmind/rmtest/config/legacyConfig.json");
        Assert.assertNotNull(configuration);
        configuration.validate();
        Assert.assertTrue(configuration.runner instanceof LocalConfiguration);
    }

    @Test
    public void systemPropertyOverride() throws IOException {
        System.setProperty("runner.useFirefox", "true");
        Configuration configuration = Configuration.read("src/test/resources/se/redmind/rmtest/config/localConfig.yml").validate();
        Assert.assertEquals(false, configuration.runner.as(LocalConfiguration.class).useFirefox);
        configuration.applySystemProperties();
        Assert.assertEquals(true, configuration.runner.as(LocalConfiguration.class).useFirefox);
    }
}
