package ninja.rail.api.pojo;

public record Leg(
        Station departure_station,
        Station arrival_station,
        String departure_date,
        String origin_departure_date
) {}
