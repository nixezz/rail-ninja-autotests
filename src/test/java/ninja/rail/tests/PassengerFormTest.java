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

import static ninja.rail.constants.Constant.TimeoutVariable.EXPLICIT_WAIT_25;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Тестирование интерфейса")
public class PassengerFormTest extends BaseSeleniumTest {

//    @Test
//    public void test1(){
//        String name = "Ivan Ivanov";
//        String passportNumber = "111111";
//        String dateOfBirth = "1990-01-01";
//        GenderEnum gender = GenderEnum.MALE;
//
//        String departureStation = "Mecca";
//        String arrivalStation = "Medina";
//        MainPage mainPage = new MainPage(driver);
//        TimetablePage timetablePage = mainPage.openMainPage()
//                .enterDepartureStation(departureStation)
//                .enterArrivalStation(arrivalStation)
//                .setFifthOfNovemberDate()
//                .searchButtonClick();
//
//        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_25);
//        try {
//            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
//        } catch (Exception e) {
//            throw new RuntimeException("The new tab didn't open", e);
//        }
//        mainPage.switchToWindow(1);
//
//        PassengersPage passengersPage = timetablePage.selectFirstTrain()
//                .clickButtonToPassengerInfo();
//        passengersPage
//                .enterPassportNumber(passportNumber)
//                .enterNameAsInPassport(name);
//    }

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

        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_25);
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

//    @Test
//    public void test2(){
//        String name = "Daria Aria";
//        String passportNumber = "111111";
//        GenderEnum gender = GenderEnum.FEMALE;
//
//        String departureStation = "Mecca";
//        String arrivalStation = "Medina";
//        MainPage mainPage = new MainPage(driver);
//        TimetablePage timetablePage = mainPage.openMainPage()
//                .enterDepartureStation(departureStation)
//                .enterArrivalStation(arrivalStation)
//                .setFifthOfNovemberDate()
//                .searchButtonClick();
//
//        WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_25);
//        try {
//            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
//        } catch (Exception e) {
//            throw new RuntimeException("The new tab didn't open", e);
//        }
//        mainPage.switchToWindow(1);
//
//        PassengersPage passengersPage = timetablePage.selectFirstTrain()
//                .clickButtonToPassengerInfo();
//
//        passengersPage.enterPassportNumber(passportNumber)
//                .enterNameAsInPassport(name)
//                .selectFirstCitizenshipInLists()
//                .enterGender(gender);
//
//
//    }
}
