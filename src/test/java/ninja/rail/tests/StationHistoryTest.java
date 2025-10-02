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
        logger.info("Инициализация конфигурации API");
        ApiConfig.init();
    }

    @Step("Кодирование JSON истории поисков в Base64 и URL-encode для cookie")
    private String encodeSearchHistory(String jsonHistory) {
        logger.info("Кодирование JSON истории поисков: {}", jsonHistory);
        String base64Encoded = Base64.getEncoder().encodeToString(jsonHistory.getBytes(StandardCharsets.UTF_8));
        String encodedCookie = URLEncoder.encode(base64Encoded, StandardCharsets.UTF_8);
        logger.debug("Закодированная cookie: {}", encodedCookie);
        return encodedCookie;
    }

    @Step("Отправка GET-запроса к эндпоинту истории с cookie")
    private Response getHistoryResponse(String cookieValue) {
        logger.info("Отправка GET-запроса с cookie: {}", cookieValue);
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
        logger.info("Начало теста: Проверка пустой истории");
        Allure.step("Отправка запроса без cookie");
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
        assertTrue(history.isEmpty(), "История должна быть пустой без cookie");
        logger.info("Тест завершен: История пустая, как ожидалось");
    }

    @Test
    @Story("Проверка одиночной истории поиска")
    @Description("Тест проверяет корректность возврата истории для одного поискового запроса")
    @DisplayName("Тест одиночной истории")
    @Owner("https://github.com/nixezz")
    public void testSingleSearchHistory() {
        logger.info("Начало теста: Проверка одиночной истории поиска");
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

        Allure.step("Установка закодированной cookie и отправка запроса");
        Response response = getHistoryResponse(encodedCookie);

        assertEquals(200, response.getStatusCode(), "Статус код должен быть 200");
        assertEquals("application/json", response.getContentType(), "Тип ответа должен быть JSON");

        List<HistoryItem> history = response.jsonPath().getList(".", HistoryItem.class);
        assertEquals(1, history.size(), "Должна быть возвращена одна запись истории");

        HistoryItem item = history.get(0);
        assertFalse(item.round_trip(), "Поиск не должен быть туда-обратно");
        assertFalse(item.complex_trip(), "Поиск не должен быть сложным");
        assertEquals(20, item.max_passengers(), "Максимальное количество пассажиров должно быть 20");
        assertEquals(1, item.legs().size(), "Должен быть один маршрут");
        assertEquals(1, item.passengers().adults(), "Должен быть один взрослый пассажир");
        logger.info("Тест завершен: Одиночная история возвращена корректно");
    }


    @Test
    @Story("Проверка множественной истории поиска")
    @Description("Тест проверяет корректность возврата истории при отправлении нескольких поисковых запросов (негативный тест)")
    @DisplayName("Негативный тест с отправкой множественной истории")
    @Owner("https://github.com/nixezz")
    public void testMultipleSearchHistory() {
        logger.info("Начало теста: Проверка множественной истории поиска");
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

        Allure.step("Установка закодированной cookie для нескольких поисков и отправка запроса");
        Response response = getHistoryResponse(encodedCookie);

        assertEquals(200, response.getStatusCode(), "Статус код должен быть 200");
        assertEquals("application/json", response.getContentType(), "Тип ответа должен быть JSON");

        List<HistoryItem> history = response.jsonPath().getList(".", HistoryItem.class);
        logger.info("Возвращено записей истории: {}", history.size());
        if (history.size() == 1) {
            HistoryItem item = history.get(0);
            logger.info("Содержимое первой записи: passengers.adults={}, departure_station={}, departure_date={}",
                    item.passengers().adults(),
                    item.legs().get("1").departure_station().uuid(),
                    item.legs().get("1").departure_date());
        }
        assertEquals(1, history.size(), "Должна быть возвращена одна запись истории");

        for (HistoryItem item : history) {
            assertFalse(item.round_trip(), "Поиск не должен быть туда-обратно");
            assertFalse(item.complex_trip(), "Поиск не должен быть сложным");
            assertEquals(20, item.max_passengers(), "Максимальное количество пассажиров должно быть 20");

            // Проверка новых полей, если они присутствуют
            if (item.legs().get("1").departure_station().address().langcode() != null) {
                logger.info("Поле langcode присутствует: {}", item.legs().get("1").departure_station().address().langcode());
            }
            if (item.legs().get("1").departure_station().geolocation().data() != null) {
                logger.info("Поле data присутствует: {}", item.legs().get("1").departure_station().geolocation().data());
            }
            if (item.legs().get("1").departure_station().parent_station() != null) {
                logger.info("Поле parent_station присутствует: {}", item.legs().get("1").departure_station().parent_station());
            }
            if (item.proceeded_with_code() != null) {
                logger.info("Поле proceeded_with_code присутствует: {}", item.proceeded_with_code());
            }
        }
        logger.info("Тест завершен: При отправке множественной истории возвращается единственная первая история");
    }

    @Test
    @Story("Проверка невалидной cookie")
    @Description("Тест проверяет, что эндпоинт корректно обрабатывает невалидную cookie")
    @DisplayName("Тест невалидной cookie")
    @Owner("https://github.com/nixezz")
    public void testInvalidCookie() {
        logger.info("Начало теста: Проверка невалидной cookie");
        String invalidCookie = "invalid_base64_string";

        Allure.step("Отправка запроса с невалидной cookie");
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
        assertTrue(history.isEmpty(), "История должна быть пустой при невалидной cookie");
        logger.info("Тест завершен: Невалидная cookie обработана корректно, история пустая");
    }
}