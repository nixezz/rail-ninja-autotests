package ninja.rail.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Station(
        @JsonProperty("uuid") String uuid,
        @JsonProperty("single_name") String single_name,
        @JsonProperty("address") Address address,
        @JsonProperty("geolocation") Geolocation geolocation,
        @JsonProperty("timezone") String timezone,
        @JsonProperty("parent_station") @JsonInclude(JsonInclude.Include.NON_NULL) String parent_station,
        @JsonProperty("description") String description,
        @JsonProperty("banner_front_page") Object banner_front_page,
        @JsonProperty("thumbnail") Object thumbnail,
        @JsonProperty("app_banner") Object app_banner,
        @JsonProperty("synchronize") boolean synchronize,
        @JsonProperty("uuid_revision") String uuid_revision
) {}
