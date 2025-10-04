package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record HistoryItem(
        @JsonProperty("legs") @JsonInclude(JsonInclude.Include.NON_NULL) Map<String, Leg> legs,
        @JsonProperty("passengers") @JsonInclude(JsonInclude.Include.NON_NULL) PassengerInfo passengers,
        @JsonProperty("round_trip") @JsonInclude(JsonInclude.Include.NON_NULL) boolean round_trip,
        @JsonProperty("complex_trip") @JsonInclude(JsonInclude.Include.NON_NULL) boolean complex_trip,
        @JsonProperty("max_passengers") @JsonInclude(JsonInclude.Include.NON_NULL) int max_passengers,
        @JsonProperty("new_search") @JsonInclude(JsonInclude.Include.NON_NULL) String new_search,
        @JsonProperty("redirect") @JsonInclude(JsonInclude.Include.NON_NULL) String redirect,
        @JsonProperty("proceeded_with_code") @JsonInclude(JsonInclude.Include.NON_NULL) String proceeded_with_code
) {}
