//
// Copyright (c) 2012 Health Market Science, Inc.
//
package no.auke.drone.ws;

import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * An object representation of the YAML configuration file.
 * 
 * @author thaihuynh
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AukeUiConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private String profile;

    public String getProfile() {
        return profile;
    }
}
