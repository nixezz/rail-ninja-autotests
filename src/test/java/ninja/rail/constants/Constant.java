package ninja.rail.constants;

import java.time.Duration;

public class Constant {
    public static class TimeoutVariable {
        public static final int IMPLICIT_WAIT = 10;
        public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(15);
        public static final Duration EXPLICIT_WAIT_25 = Duration.ofSeconds(25);
        public static final int PAGE_LOAD_TIMEOUT = 15;
    }

    public static class Urls {
        public static final String MAIN_PAGE = "https://rail.ninja";
        public static final String TIMETABLE_PAGE = "https://rail.ninja/trains/order/timetable";
        public static final String TIMETABLE_V9_PAGE = "https://rail.ninja/v9/trains/order/timetable";
        public static final String PASSENGERS_FORM_PAGE = "https://rail.ninja/trains/order/{}/passenger";
        public static final String PASSENGERS_FORM_V9_PAGE = "https://rail.ninja/v9/trains/order/{}/passenger";
    }

    public static class API {
        public static final String HOST = "https://back.rail.ninja";
    }

    public static class Endpoints {
        public static final String TIMETABLE_PATH = "/api/v2/timetable";
        public static final String HISTORY_PATH = "/api/v1/station/history";
    }
}
