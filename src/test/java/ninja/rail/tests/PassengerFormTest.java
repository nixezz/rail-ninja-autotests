package ninja.rail.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import ninja.rail.core.BaseSeleniumTest;
import ninja.rail.pages.MainPage;
import ninja.rail.pages.pasengers.PassengersPage;
import ninja.rail.pages.timetable.TimetablePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static ninja.rail.constants.Constant.TimeoutVariable.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Тестирование интерфейса")
public class PassengerFormTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Указание даты рождения пассажира младше 12 лет")
    @Description("При указании возраста младше 12 лет, но старше 3 лет, заголовок должен измениться на Child 1 и рядом должен быть указан возраст")
    public void passengerForm_ChildAge_changeHeaderTest(){
        String expectedHeader = "Child 1";
        String departureStation = "Mecca";
        String arrivalStation = "Medina";
        String dayOfBirth = "10.05.2015";
        String expectedAgeNotify = "(Age: 10)";
        MainPage mainPage = new MainPage(driver);
        TimetablePage timetablePage = mainPage.openMainPage()
                .enterDepartureStation(departureStation)
                .enterArrivalStation(arrivalStation)
                .setFifthOfNovemberDate()
                .searchButtonClick();

        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_60);
        try {
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        } catch (Exception e) {
            throw new RuntimeException("The new tab didn't open", e);
        }
        mainPage.switchToWindow(1);

        PassengersPage passengersPage = timetablePage.selectFirstTrain()
                .clickButtonToPassengerInfo();

        PassengersPage passengersPageResult = passengersPage.enterBirthdayDate(dayOfBirth);
        String headerText = passengersPageResult.getHeader();
        assertEquals(expectedHeader, headerText,"The title 'Child 1' was expected, but it wasn't confirmed.");
        String ageNotifyText = passengersPageResult.getAgeNotify();
        assertEquals(expectedAgeNotify, ageNotifyText, "The age notify title '(Age: 10)' was expected, but it wasn't confirmed.");
    }

    @Test
    @DisplayName("Указание даты рождения пассажира младше 3 лет")
    @Description("При указании возраста младше 3 лет, заголовок должен измениться на Infant 1 и рядом должен быть указан возраст")
    public void passengerForm_InfantAge_changeHeaderTest(){
        String expectedHeader = "Infant 1";
        String departureStation = "Mecca";
        String arrivalStation = "Medina";
        String dayOfBirth = "10.05.2024";
        String expectedAgeNotify = "(Age: 1)";
        MainPage mainPage = new MainPage(driver);
        TimetablePage timetablePage = mainPage.openMainPage()
                .enterDepartureStation(departureStation)
                .enterArrivalStation(arrivalStation)
                .setFifthOfNovemberDate()
                .searchButtonClick();

        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_60);
        try {
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        } catch (Exception e) {
            throw new RuntimeException("The new tab didn't open", e);
        }
        mainPage.switchToWindow(1);

        PassengersPage passengersPage = timetablePage.selectFirstTrain()
                .clickButtonToPassengerInfo();

        PassengersPage passengersPageResult = passengersPage.enterBirthdayDate(dayOfBirth);
        String headerText = passengersPageResult.getHeader();
        assertEquals(expectedHeader, headerText,"The title 'Infant 1' was expected, but it wasn't confirmed.");
        String ageNotifyText = passengersPageResult.getAgeNotify();
        assertEquals(expectedAgeNotify, ageNotifyText, "The age notify title '(Age: 1)' was expected, but it wasn't confirmed.");
    }

    @Test
    @DisplayName("Проверка появления подсказки при использовании некорректных символов в имени пассажира")
    @Description("При вводе в поле имени пассажира НЕ латинских символов должно появится уведомление (подсказка) под полем ввода 'Use latin letters. Do not use special characters'")
    public void passengerForm_IncorrectPassengerName_checkNotification(){
        String incorrectName = "123";
        String expectedNotificationText = "Use latin letters. Do not use special characters";
        String departureStation = "Mecca";
        String arrivalStation = "Medina";

        MainPage mainPage = new MainPage(driver);
        TimetablePage timetablePage = mainPage.openMainPage()
                .enterDepartureStation(departureStation)
                .enterArrivalStation(arrivalStation)
                .setFifthOfNovemberDate()
                .searchButtonClick();

        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_60);
        try {
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        } catch (Exception e) {
            throw new RuntimeException("The new tab didn't open", e);
        }
        mainPage.switchToWindow(1);

        PassengersPage passengersPage = timetablePage.selectFirstTrain()
                .clickButtonToPassengerInfo();

        passengersPage.enterNameAsInPassport(incorrectName);
        String notificationText = passengersPage.getIncorrectNameNotification();
        assertEquals(expectedNotificationText, notificationText,"The notification 'Use latin letters. Do not use special characters' was expected, but it wasn't confirmed.");
    }

    @Test
    @DisplayName("Проверка появления подсказки при неполном указании данных в имени пассажира")
    @Description("При вводе в имени пассажира одного слова должно появится уведомление (подсказка) под полем ввода 'Enter first and last name'")
    public void passengerForm_IncompletePassengerName_checkNotification(){
        String incorrectName = "Name";
        String expectedNotificationText = "Enter first and last name";
        String departureStation = "Mecca";
        String arrivalStation = "Medina";

        MainPage mainPage = new MainPage(driver);
        TimetablePage timetablePage = mainPage.openMainPage()
                .enterDepartureStation(departureStation)
                .enterArrivalStation(arrivalStation)
                .setFifthOfNovemberDate()
                .searchButtonClick();

        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_60);
        try {
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        } catch (Exception e) {
            throw new RuntimeException("The new tab didn't open", e);
        }
        mainPage.switchToWindow(1);

        PassengersPage passengersPage = timetablePage.selectFirstTrain()
                .clickButtonToPassengerInfo();

        passengersPage.enterNameAsInPassport(incorrectName);
        String notificationText = passengersPage.getIncorrectNameNotification();
        assertEquals(expectedNotificationText, notificationText,"The notification 'Enter first and last name' was expected, but it wasn't confirmed.");
    }
}
