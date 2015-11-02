package se.redmind.rmtest.config;


import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Jeremy Comte
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class RunnerConfiguration {

    public <SubType extends RunnerConfiguration> SubType as(Class<SubType> clazz) {
        return (SubType) this;
    }

}
