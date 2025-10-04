package ninja.rail.tests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ninja.rail.constants.ApiConfig;
import ninja.rail.api.pojo.HistoryItem;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static ninja.rail.constants.Constant.Endpoints.HISTORY_PATH;

@Epic("Тестирование API")
@Feature("Эндпоинт истории поисков")
public class StationHistoryTest {
    private static final Logger logger = LoggerFactory.getLogger(StationHistoryTest.class);

    @BeforeAll
    public static void setupApiConfig() {
        logger.info("Initializing the API configuration");
        ApiConfig.init();
    }

    @Step("Кодирование JSON истории поисков в Base64 и URL-encode для cookie")
    private String encodeSearchHistory(String jsonHistory) {
        logger.info("Encoding the JSON search history: {}", jsonHistory);
        String base64Encoded = Base64.getEncoder().encodeToString(jsonHistory.getBytes(StandardCharsets.UTF_8));
        String encodedCookie = URLEncoder.encode(base64Encoded, StandardCharsets.UTF_8);
        logger.debug("Encoded cookie: {}", encodedCookie);
        return encodedCookie;
    }

    @Step("Отправка GET-запроса к эндпоинту истории с cookie")
    private Response getHistoryResponse(String cookieValue) {
        logger.info("Sending a GET request with a cookie: {}", cookieValue);
        return given()
                .spec(ApiConfig.getRequestSpec())
                .cookie("search_history", cookieValue)
                .when()
                .get(HISTORY_PATH)
                .then()
                .extract()
                .response();
    }

    @Test
    @Story("Проверка пустой истории")
    @Description("Тест проверяет, что эндпоинт возвращает пустой массив, если cookie с историей отсутствует")
    @DisplayName("Тест пустой истории")
    @Owner("https://github.com/nixezz")
    public void testEmptyHistory() {
        logger.info("Checking an empty history");
        Allure.step("Sending a request without cookies");
        Response response = given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get(HISTORY_PATH)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        List<HistoryItem> history = response.jsonPath().getList(".", HistoryItem.class);
        assertTrue(history.isEmpty(), "The history should be empty without cookies.");
        logger.info("Test completed: The story is empty, as expected");
    }

    @Test
    @Story("Проверка одиночной истории поиска")
    @Description("Тест проверяет корректность возврата истории для одного поискового запроса")
    @DisplayName("Тест одиночной истории")
    @Owner("https://github.com/nixezz")
    public void testSingleSearchHistory() {
        logger.info("Checking a single search history");
        String jsonHistory = """
                [{
                    "passengers": {"adults": 1, "children": 0, "children_age": []},
                    "form-mode": "basic-mode",
                    "legs": {
                      "1": {
                        "departure_station": "672",
                        "arrival_station": "580",
                        "departure_date": "2025-12-31"
                      }
                    }
                  }]
                """;
        String encodedCookie = encodeSearchHistory(jsonHistory);

        Allure.step("Setting an encoded cookie and sending a request");
        Response response = getHistoryResponse(encodedCookie);

        assertEquals(200, response.getStatusCode(), "The status code should be 200");
        assertEquals("application/json", response.getContentType(), "The response type must be JSON");

        List<HistoryItem> history = response.jsonPath().getList(".", HistoryItem.class);
        assertEquals(1, history.size(), "One history record should be returned.");

        HistoryItem item = history.get(0);
        assertFalse(item.round_trip(), "The search doesn't have to be round-trip");
        assertFalse(item.complex_trip(), "The search doesn't have to be complicated.");
        assertEquals(20, item.max_passengers(), "The maximum number of passengers should be 20"); /// ?
        assertEquals(1, item.legs().size(), "There must be one route.");
        assertEquals(1, item.passengers().adults(), "There must be one adult passenger.");
        logger.info("Test completed: Single story returned correctly");
    }


    @Test
    @Story("Проверка множественной истории поиска")
    @Description("Тест проверяет корректность возврата истории при отправлении нескольких поисковых запросов (негативный тест)")
    @DisplayName("Негативный тест с отправкой множественной истории")
    @Owner("https://github.com/nixezz")
    public void testMultipleSearchHistory() {
        logger.info("Checking multiple search history");
        String jsonHistory = """
                [{
                    "passengers": {"adults": 1, "children": 0, "children_age": []},
                    "form-mode": "basic-mode",
                    "legs": {
                      "1": {
                        "departure_station": "672",
                        "arrival_station": "580",
                        "departure_date": "2025-12-31"
                      }
                    }
                  },
                  {
                    "passengers": {"adults": 2, "children": 1, "children_age": [5]},
                    "form-mode": "basic-mode",
                    "legs": {
                      "1": {
                        "departure_station": "100",
                        "arrival_station": "200",
                        "departure_date": "2026-01-01"
                      }
                    }
                  }]
                """;
        String encodedCookie = encodeSearchHistory(jsonHistory);

        Allure.step("Setting an encoded cookie for multiple searches and sending a request");
        Response response = getHistoryResponse(encodedCookie);

        assertEquals(200, response.getStatusCode(), "The status code should be 200");
        assertEquals("application/json", response.getContentType(), "The response type must be JSON");

        List<HistoryItem> history = response.jsonPath().getList(".", HistoryItem.class);
        logger.info("History records returned: {}", history.size());
        if (history.size() == 1) {
            HistoryItem item = history.get(0);
            logger.info("Contents of the first record: passengers.adults={}, departure_station={}, departure_date={}",
                    item.passengers().adults(),
                    item.legs().get("1").departure_station().uuid(),
                    item.legs().get("1").departure_date());
        }
        assertEquals(1, history.size(), "One history record should be returned");

        for (HistoryItem item : history) {
            assertFalse(item.round_trip(), "The search doesn't have to be round-trip");
            assertFalse(item.complex_trip(), "The search doesn't have to be complicated");
            assertEquals(20, item.max_passengers(), "МThe maximum number of passengers should be 20");
        }
        logger.info("The test is completed: When sending multiple stories, the only first story is returned.");
    }

    @Test
    @Story("Проверка невалидной cookie")
    @Description("Тест проверяет, что эндпоинт корректно обрабатывает невалидную cookie")
    @DisplayName("Тест невалидной cookie")
    @Owner("https://github.com/nixezz")
    public void testInvalidCookie() {
        logger.info("Checking an invalid cookie");
        String invalidCookie = "invalid_base64_string";

        Allure.step("Sending a request with an invalid cookie");
        Response response = given()
                .spec(ApiConfig.getRequestSpec())
                .cookie("search_history", invalidCookie)
                .when()
                .get(HISTORY_PATH)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        List<HistoryItem> history = response.jsonPath().getList(".", HistoryItem.class);
        assertTrue(history.isEmpty(), "The history should be empty with an invalid cookie");
        logger.info("The test is completed: Invalid cookie was processed correctly, the history is empty");
    }
}