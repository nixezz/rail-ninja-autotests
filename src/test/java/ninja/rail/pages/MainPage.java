package ninja.rail.pages;

import io.qameta.allure.Step;
import ninja.rail.core.BaseSeleniumPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static ninja.rail.constants.Constant.Urls.TIMETABLE_PAGE;
import static ninja.rail.constants.Constant.Urls.TIMETABLE_V9_PAGE;

public class MainPage extends BaseSeleniumPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainPage.class);

    @FindBy(xpath = "//*[@id='departure_station']")
    private WebElement departureStationInput;

    @FindBy(xpath = "//*[@id='arrival_station']")
    private WebElement arrivalStationInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@label='Travelers']")
    private WebElement travelersButton;

    @FindBy(xpath = "//div[contains(@class, 'eLKYXC')]")
    private WebElement calendarButton;

    @FindBy(xpath = "//button[@aria-label='next-year']")
    private WebElement calendarNextMonthButton;

    @FindBy(xpath = "//button[@aria-label='prev-year']")
    private WebElement calendarPreviousMonthButton;

    @FindBy(xpath = "//button[@aria-label='month panel']")
    private WebElement monthPanelButton;

    @FindBy(xpath = "//button[@aria-label='year panel']")
    private WebElement yearPanelButton;

    @FindBy(xpath = "//*[@id='search-form-rn-modern_adults']")
    private WebElement adultPassengerCounter;

    @FindBy(xpath = "/html/body/div[1]/div/div/header/div[2]/div[2]/form/div[4]/div/div/div/div/div[1]/div[1]/div/div/div/div/div/div/div/div[3]/button")
    private WebElement incrementAdultPassengerCounter;

    @FindBy(xpath = "/html/body/div[1]/div/div/header/div[2]/div[2]/form/div[4]/div/div/div/div/div[1]/div[1]/div/div/div/div/div/div/div/div[1]/button")
    private WebElement decrementAdultPassengerCounter;

    @FindBy(xpath = "//div[contains(@class, 'gmmcjF')]")
    private WebElement backgroundDiv;

    @FindBy(xpath = "//td[@title='2025-11-05']")
    private WebElement dateDiv;

    public MainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Открытие главной страницы https://rail.ninja")
    public MainPage openMainPage() {
        driver.get(BASE_URL);
        PageFactory.initElements(driver, this);
        return this;
    }

    @Step("Выбор пункта отправления: {stationName}")
    public MainPage enterDepartureStation(String stationName) {
        LOGGER.info("Ввод пункта отправления: {}", stationName);
        try{
            departureStationInput.click();
            departureStationInput.clear();
            departureStationInput.sendKeys(stationName);
            Thread.sleep(1000);
            departureStationInput.sendKeys(Keys.ENTER);
        } catch (InterruptedException e){
            LOGGER.error("Произошла ошибка при выборе города отправления: {}", stationName, e);
            throw new RuntimeException("Произошла ошибка при выборе города отправления: " + stationName, e);
        }
        return this;
    }

    @Step("Выбор пункта прибытия: {stationName}")
    public MainPage enterArrivalStation(String stationName) {
        LOGGER.info("Ввод пункта прибытия: {}", stationName);
        try{
            waitForElementVisible(arrivalStationInput);
            waitForElementClickable(arrivalStationInput);
            arrivalStationInput.click();
            arrivalStationInput.clear();
            arrivalStationInput.sendKeys(stationName);
            Thread.sleep(1000);
            arrivalStationInput.sendKeys(Keys.ENTER);
        } catch (InterruptedException e){
            LOGGER.error("Произошла ошибка при выборе города отправления: {}", stationName, e);
            throw new RuntimeException("Произошла ошибка при выборе города отправления: " + stationName, e);
        }
        return this;
    }

    @Step("Выбор двух взрослых пассажиров")
    public MainPage setTwoAdultPassenger(){
        LOGGER.info("Установка двух взрослых пассажиров.");
        try{
            waitForElementVisible(travelersButton);
            waitForElementClickable(travelersButton);
            travelersButton.click();
            waitForElementVisible(incrementAdultPassengerCounter);
            waitForElementClickable(incrementAdultPassengerCounter);
            incrementAdultPassengerCounter.click();
            Thread.sleep(500);
            waitForElementVisible(incrementAdultPassengerCounter);
            waitForElementClickable(incrementAdultPassengerCounter);
            Thread.sleep(500);
            backgroundDiv.click();
        } catch (InterruptedException e){
            LOGGER.error("Произошла ошибка при установлении количества пассажиров: ", e);
            throw new RuntimeException("Произошла ошибка при установлении количества пассажиров: ", e);
        }
        return this;
    }

    @Step("Установка даты в календаре (5 ноября 2025)")
    public MainPage setFifthOfNovemberDate(){
        LOGGER.info("Установка даты (5 ноября 2025).");
        try{
            waitForElementVisible(calendarButton);
            waitForElementClickable(calendarButton);
            calendarButton.click();
            waitForElementVisible(calendarNextMonthButton);
            waitForElementClickable(calendarNextMonthButton);
            calendarNextMonthButton.click();
            Thread.sleep(500);
            waitForElementVisible(dateDiv);
            waitForElementClickable(dateDiv);
            dateDiv.click();
        } catch (InterruptedException e){
            LOGGER.error("Произошла ошибка при установке даты (5 ноября 2025): ", e);
            throw new RuntimeException("Произошла ошибка при установке даты (5 ноября 2025): ", e);
        }
        return this;
    }

    @Step("Нажатие на кнопку 'Search trains'")
    public TimetablePage searchButtonClick(){
        LOGGER.info("Нажатие на кнопку 'Search trains'. Переход на страницу {}", TIMETABLE_V9_PAGE);
        try {
            waitForElementClickable(submitButton);
            submitButton.click();
            return switchMonitorToTimetablePage();
        } catch (InterruptedException e){
            LOGGER.error("При нажатии на кнопку поиска произошла ошибка: ", e);
            throw new RuntimeException("При нажатии на кнопку поиска произошла ошибка:", e);
        }
    }

    @Step("Переключение на страницу расписания поездов")
    public TimetablePage switchMonitorToTimetablePage() {
        LOGGER.info("Переключение монитора на странницу: {}", TIMETABLE_V9_PAGE);
        try {
            String originalWindow = driver.getWindowHandle();
            Set<String> allWindows = driver.getWindowHandles();
            if (allWindows.size() > 1) {
                for (String windowHandle : allWindows) {
                    if (!windowHandle.equals(originalWindow)) {
                        driver.switchTo().window(windowHandle);
                        break;
                    }
                }
            }
            waitForUrlContains(TIMETABLE_V9_PAGE);
            LOGGER.info("Переключение на другую страницу произошло успешно. Текущее URL: {}", driver.getCurrentUrl());
            return new TimetablePage(driver);
        } catch (Exception e) {
            LOGGER.error("При переключении монитора на другую страницу произошла ошибка: ", e);
            throw new RuntimeException("При переключении монитора на другую страницу произошла ошибка: " + TIMETABLE_V9_PAGE, e);
        }
    }



    private void waitForElementVisible(WebElement element) throws InterruptedException {
        for (int i = 0; i < 30; i++) { // Max 15 seconds (30 * 500ms)
            if (element.isDisplayed()) {
                return;
            }
            Thread.sleep(500);
        }
        throw new TimeoutException("Element not visible: " + element);
    }

    private void waitForElementClickable(WebElement element) throws InterruptedException {
        for (int i = 0; i < 30; i++) { // Max 15 seconds (30 * 500ms)
            if (element.isEnabled() && element.isDisplayed()) {
                return;
            }
            Thread.sleep(500);
        }
        throw new TimeoutException("Element not clickable: " + element);
    }

    private void waitForUrlContains(String urlPart) throws InterruptedException {
        for (int i = 0; i < 30; i++) { // Max 15 seconds (30 * 500ms)
            if (driver.getCurrentUrl().contains(urlPart)) {
                return;
            }
            Thread.sleep(500);
        }
        throw new TimeoutException("URL does not contain: " + urlPart);
    }
}
