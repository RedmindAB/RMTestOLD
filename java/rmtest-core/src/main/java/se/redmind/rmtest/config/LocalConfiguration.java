package se.redmind.rmtest.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Jeremy Comte
 */
@JsonTypeName("local")
public class LocalConfiguration extends RunnerConfiguration {

    @JsonProperty
    public boolean usePhantomJS;

    @JsonProperty
    public boolean useChrome;

    @JsonProperty
    public boolean useFirefox;

    @JsonProperty
    public AndroidConfiguration android;

}
