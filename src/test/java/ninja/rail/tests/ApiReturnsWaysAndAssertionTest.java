package ninja.rail.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ninja.rail.core.BaseSeleniumTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static ninja.rail.constants.Constant.API.HOST;
import static ninja.rail.constants.Constant.Endpoints.TIMETABLE_PATH;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Тестирование API")
public class ApiReturnsWaysAndAssertionTest extends BaseSeleniumTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiReturnsWaysAndAssertionTest.class);
    private static final String API_USER_KEY = "4ae3369b0952f1c1176deec94708f3a7";
    private static final String CURRENCY = "USD";
    private static final String UUID_DEPARTURE_STATION = "23e9ca21-c51d-41be-b421-94e2da736ce3";
    private static final String UUID_ARRIVAL_STATION = "8fbfe521-8d0c-4187-9076-ad1731b42ae9";
    private static final String DATE_VALUE = "05.11.2025";

    @Test
    @Owner("https://github.com/nixezz")
    @DisplayName("ТС-2: Корректное возвращение расписания поездов через API POST")
    @Description("Проверить, что API POST /api/v2/timetable возвращает рейсы, в которых станции отправления/прибытия и дата соответствуют значениям, переданным в запросе, и что структура/типы данных корректны.\nОжидаемый результат\n" +
            "1.\tHTTP-код: 200 OK.\n" +
            "2.\tContent-Type: application/json.\n" +
            "3.\tВ ответе есть данные о рейсах.\n" +
            "4.\tДля каждого найденного рейса:\n" +
            "departure_station.single_name = Mecca\n" +
            "arrival_station.single_name = Medina\n" +
            "departure_date = 05.11.2025\n")
    public void apiReturnsWaysAndAssertionTest() {
        String requestBody = buildRequestBody();
        Response response = sendPostRequest(requestBody);
        verifyResponse(response);
    }

    @Step("Формирование тела запроса")
    private String buildRequestBody() {
        return """
                {
                  "passengers": { "adults": 1, "children": 0, "children_age": [] },
                  "legs": {
                    "1": {
                      "departure_station": "%s",
                      "arrival_station": "%s",
                      "departure_date": "%s"
                    }
                  }
                }
                """.formatted(UUID_DEPARTURE_STATION, UUID_ARRIVAL_STATION, DATE_VALUE);
    }

    @Step("Отправка POST-запроса на {0}")
    private Response sendPostRequest(String requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("X-currency", CURRENCY)
                .header("X-API-User-Key", API_USER_KEY)
                .body(requestBody)
                .when()
                .post(HOST + TIMETABLE_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Проверка ответа API")
    private void verifyResponse(Response response) {
        verifyStatusCode(response);
        verifyContentType(response);
        verifyTrainsPresence(response);
        verifyTrainsDetails(response);
    }

    @Step("Проверка HTTP-кода")
    private void verifyStatusCode(Response response) {
        assertEquals(200, response.getStatusCode(), "Expected HTTP status 200 OK");
    }

    @Step("Проверка Content-Type")
    private void verifyContentType(Response response) {
        String contentType = response.getHeader("Content-Type");
        assertTrue(contentType.contains("application/json"), "Ожидался Content-Type в application/json, но получен: " + contentType);
    }

    @Step("Проверка наличия рейсов")
    private void verifyTrainsPresence(Response response) {
        Object trainsObj = response.jsonPath().get("trains");
        assertNotNull(trainsObj, "Поле 'trains' отсутствует в ответе");
    }

    @Step("Проверка деталей рейсов")
    private void verifyTrainsDetails(Response response) {
        Object trainsObj = response.jsonPath().get("trains");
        List<Object> trains;
        if (trainsObj instanceof List) {
            trains = response.jsonPath().getList("trains");
        } else if (trainsObj instanceof Map) {
            Map<String, Object> trainsMap = response.jsonPath().getMap("trains");
            trains = List.copyOf(trainsMap.values());
        } else {
            fail("Field 'trains' is neither a List nor a Map: " + trainsObj.getClass());
            return;
        }

        assertFalse(trains.isEmpty(), "Ответ должен содержать хотя бы один поезд.");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String expectedDate = DATE_VALUE;

        for (int i = 0; i < trains.size(); i++) {
            Map<String, Object> train = (Map<String, Object>) trains.get(i);

            @SuppressWarnings("unchecked")
            Map<String, String> departureStation = (Map<String, String>) train.get("departure_station");
            assertNotNull(departureStation, "Станция отправления пропущена для поезда " + i);
            assertEquals("Mecca station", departureStation.get("single_name"), "Станция отправления должна быть 'Mecca station' для поезда " + i);

            @SuppressWarnings("unchecked")
            Map<String, String> arrivalStation = (Map<String, String>) train.get("arrival_station");
            assertNotNull(arrivalStation, "Станция прибытия пропущена для поезда " + i);
            assertEquals("Medina station", arrivalStation.get("single_name"), "Станция прибытия должна быть 'Medina station' для поезда " + i);

            String departureDatetime = (String) train.get("departure_datetime");
            assertNotNull(departureDatetime, "Дата отправления пропущена для поезда " + i);
            String departureDate = departureDatetime.split("T")[0];
            LocalDate date = LocalDate.parse(departureDate);
            String formattedDate = date.format(formatter);
            assertEquals(expectedDate, formattedDate, "Дата отправления должна быть 05.11.2025 для поезда " + i);
        }
    }
}
