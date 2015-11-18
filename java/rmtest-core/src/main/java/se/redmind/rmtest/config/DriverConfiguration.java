package se.redmind.rmtest.config;

import se.redmind.rmtest.DriverWrapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Jeremy Comte
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class DriverConfiguration<DriverType extends WebDriver> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final DesiredCapabilities baseCapabilities;
    private List<DriverWrapper<DriverType>> wrappers;

    @JsonProperty("capabilities")
    public Map<String, ?> configurationCapabilities = new LinkedHashMap<>();

    protected DriverConfiguration(DesiredCapabilities baseCapabilities) {
        this.baseCapabilities = baseCapabilities;
        configurationCapabilities.forEach((key, value) -> this.baseCapabilities.setCapability(key, value));
    }

    public <SubType extends DriverConfiguration<?>> SubType as(Class<SubType> clazz) {
        return (SubType) this;
    }

    public List<DriverWrapper<DriverType>> wrappers() {
        if (wrappers == null) {
            wrappers = createDrivers();
        }
        return wrappers;
    }

    protected abstract List<DriverWrapper<DriverType>> createDrivers();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        if (wrappers != null) {
            wrappers.forEach(wrapper -> stringBuilder.append(wrapper.getDescription()).append(", "));
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        return stringBuilder.append("]").toString();
    }

    public void stopDriver() {
        if (wrappers != null) {
            wrappers.stream().forEach(wrapper -> wrapper.stopDriver());
        }
    }
}
