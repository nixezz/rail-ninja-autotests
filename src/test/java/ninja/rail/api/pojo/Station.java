package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Station(
        @JsonProperty("uuid") @JsonInclude(JsonInclude.Include.NON_NULL) String uuid,
        @JsonProperty("single_name") @JsonInclude(JsonInclude.Include.NON_NULL) String single_name,
        @JsonProperty("address") @JsonInclude(JsonInclude.Include.NON_NULL) Address address,
        @JsonProperty("geolocation") @JsonInclude(JsonInclude.Include.NON_NULL) Geolocation geolocation,
        @JsonProperty("timezone") @JsonInclude(JsonInclude.Include.NON_NULL) String timezone,
        @JsonProperty("parent_station") @JsonInclude(JsonInclude.Include.NON_NULL) String parent_station,
        @JsonProperty("description") @JsonInclude(JsonInclude.Include.NON_NULL) String description,
        @JsonProperty("banner_front_page") @JsonInclude(JsonInclude.Include.NON_NULL) Object banner_front_page,
        @JsonProperty("thumbnail") @JsonInclude(JsonInclude.Include.NON_NULL) Object thumbnail,
        @JsonProperty("app_banner") @JsonInclude(JsonInclude.Include.NON_NULL) Object app_banner,
        @JsonProperty("synchronize") @JsonInclude(JsonInclude.Include.NON_NULL) boolean synchronize,
        @JsonProperty("uuid_revision") @JsonInclude(JsonInclude.Include.NON_NULL) String uuid_revision
) {}
