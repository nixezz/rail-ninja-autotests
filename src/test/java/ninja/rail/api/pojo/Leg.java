package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

public record Leg(
        @JsonInclude(JsonInclude.Include.NON_NULL) Station departure_station,
        @JsonInclude(JsonInclude.Include.NON_NULL) Station arrival_station,
        @JsonInclude(JsonInclude.Include.NON_NULL) String departure_date,
        String origin_departure_date
) {}
