package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Address(
        @JsonProperty("country_code") String country_code,
        @JsonProperty("langcode") @JsonInclude(JsonInclude.Include.NON_NULL) String langcode,
        @JsonProperty("locality") String locality,
        @JsonProperty("address_line1") String address_line1
) {}
