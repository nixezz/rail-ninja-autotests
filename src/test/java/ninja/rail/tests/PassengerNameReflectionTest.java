package ninja.rail.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import ninja.rail.core.BaseSeleniumTest;
import ninja.rail.pages.MainPage;
import ninja.rail.pages.pasengers.PassengersPage;
import ninja.rail.pages.timetable.TimetablePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static ninja.rail.constants.Constant.TimeoutVariable.EXPLICIT_WAIT_25;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        TimetablePage timetablePage = mainPage.openMainPage()
                .enterDepartureStation(departureStation)
                .enterArrivalStation(arrivalStation)
                .setFifthOfNovemberDate()
                .searchButtonClick();

        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_25);
        try {
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        } catch (Exception e) {
            throw new RuntimeException("The new tab didn't open", e);
        }
        mainPage.switchToWindow(1);

        PassengersPage passengerPage = timetablePage.selectFirstTrain()
                .clickButtonToPassengerInfo();
        PassengersPage passengerPageResult = passengerPage.enterNameAsInPassport(passengerName);
        String headerText = passengerPageResult.getHeader();


        assertEquals(headerText, passengerName, "There was no reflection on the change of Adult passenger 1 to the value of Name as in passport.");
    }
}