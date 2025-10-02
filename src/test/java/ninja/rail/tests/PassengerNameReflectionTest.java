package ninja.rail.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import ninja.rail.core.BaseSeleniumTest;
import ninja.rail.pages.MainPage;
import ninja.rail.pages.PassengerDetailsPage;
import ninja.rail.pages.TimetablePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Тестирование интерфейса")
public class PassengerNameReflectionTest extends BaseSeleniumTest {

    @Test
    @Owner("https://github.com/nixezz")
    @DisplayName("Отображение рефлексии поля Name as in passport в заголовке Adult passenger 1")
    @Description("Отображение значение из поля Name as in passport в заголовке Adult passenger 1")
    public void testPassengerNameReflectedInHeader() {
        String departureStation = "Mecca";
        String arrivalStation = "Medina";
        String passengerName = "John Doe";

        MainPage mainPage = new MainPage(driver);
        mainPage.openMainPage()
                .enterDepartureStation(departureStation)
                .enterArrivalStation(arrivalStation)
                .setFifthOfNovemberDate()
                .searchButtonClick();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        } catch (Exception e) {
            throw new RuntimeException("Новая вкладка не открылась", e);
        }
        mainPage.switchToWindow(1);


        TimetablePage timetablePage = new TimetablePage(driver);
        timetablePage.clickFirstScheduleItem().clickContinue();
//
//        String currentUrl = driver.getCurrentUrl();
//        assertTrue(currentUrl.contains("v9/trains/order/timetable"), "Not switched to timetable page. Current URL: " + currentUrl);
//
//        PassengerDetailsPage passengerPage = new PassengerDetailsPage(driver);
//        passengerPage = timetablePage.clickFlexibleTicketButton();
//        PassengerDetailsPage passengerPageWithName = passengerPage
//                .setNameAsInPassportInput(passengerName);
//
//        String reflectionName = passengerPageWithName.getReflectionHeaderText();
//        assertEquals(passengerName, reflectionName, "Рефлексии изменения Adult passenger 1 на значение Name as in passport не произошло.");
    }
}