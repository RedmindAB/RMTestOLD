package se.redmind.rmtest.config;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Jeremy Comte
 */
@JsonTypeName("grid")
public class GridConfiguration extends RunnerConfiguration {

    @JsonProperty
    @NotNull
    public String localIp;

    @JsonProperty
    @NotNull
    public String hubIp;

    @JsonProperty
    public boolean enableLiveStream;

}
