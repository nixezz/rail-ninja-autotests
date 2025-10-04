package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Address(
        @JsonProperty("country_code") @JsonInclude(JsonInclude.Include.NON_NULL) String country_code,
        @JsonProperty("langcode") @JsonInclude(JsonInclude.Include.NON_NULL) String langcode,
        @JsonProperty("locality") @JsonInclude(JsonInclude.Include.NON_NULL) String locality,
        @JsonProperty("address_line1") @JsonInclude(JsonInclude.Include.NON_NULL) String address_line1
) {}
