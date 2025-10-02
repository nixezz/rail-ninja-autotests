package ninja.rail.api.pojo;

import java.util.List;

public record PassengerInfo(
        int adults,
        int children,
        List<Integer> children_age
) {}
