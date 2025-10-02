package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record HistoryItem(
        @JsonProperty("legs") Map<String, Leg> legs,
        @JsonProperty("passengers") PassengerInfo passengers,
        @JsonProperty("round_trip") boolean round_trip,
        @JsonProperty("complex_trip") boolean complex_trip,
        @JsonProperty("max_passengers") int max_passengers,
        @JsonProperty("new_search") String new_search,
        @JsonProperty("redirect") String redirect,
        @JsonProperty("proceeded_with_code") @JsonInclude(JsonInclude.Include.NON_NULL) String proceeded_with_code
) {}
