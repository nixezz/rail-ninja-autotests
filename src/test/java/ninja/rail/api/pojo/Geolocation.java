package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Geolocation(
        @JsonProperty("lat") @JsonInclude(JsonInclude.Include.NON_NULL) double lat,
        @JsonProperty("lng") @JsonInclude(JsonInclude.Include.NON_NULL) double lng,
        @JsonProperty("value") @JsonInclude(JsonInclude.Include.NON_NULL) String value,
        @JsonProperty("data") @JsonInclude(JsonInclude.Include.NON_NULL) List<String> data
) {}
