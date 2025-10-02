package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Geolocation(
        @JsonProperty("lat") double lat,
        @JsonProperty("lng") double lng,
        @JsonProperty("value") String value,
        @JsonProperty("data") @JsonInclude(JsonInclude.Include.NON_NULL) List<String> data
) {}
