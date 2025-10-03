package ninja.rail.pages;

import io.qameta.allure.Step;
import ninja.rail.core.BaseSeleniumPage;
import ninja.rail.pages.timetable.TimetableLegacyPage;
import ninja.rail.pages.timetable.TimetablePage;
import ninja.rail.pages.timetable.TimetableV9Page;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static ninja.rail.constants.Constant.TimeoutVariable.EXPLICIT_WAIT;
import static ninja.rail.constants.Constant.TimeoutVariable.EXPLICIT_WAIT_25;
import static ninja.rail.constants.Constant.Urls.TIMETABLE_PAGE;
import static ninja.rail.constants.Constant.Urls.TIMETABLE_V9_PAGE;

public class MainPage extends BaseSeleniumPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainPage.class);
    private final WebDriverWait wait;

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
        wait = new WebDriverWait(driver, EXPLICIT_WAIT);
    }

    @Step("Открытие главной страницы https://rail.ninja")
    public MainPage openMainPage() {
        LOGGER.info("Opening the main page: {}", BASE_URL);
        driver.get(BASE_URL);
        PageFactory.initElements(driver, this);
        return this;
    }

    @Step("Выбор пункта отправления: {stationName}")
    public MainPage enterDepartureStation(String stationName) {
        LOGGER.info("Entering the departure point: {}", stationName);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(departureStationInput)).click();
            departureStationInput.clear();
            departureStationInput.sendKeys(stationName);
            Thread.sleep(1000);
            departureStationInput.sendKeys(Keys.ENTER);
            LOGGER.info("Departure point '{}' selected.", stationName);
        } catch (Exception e) {
            LOGGER.error("Error when selecting the departure point '{}': {}", stationName, e.getMessage());
            throw new RuntimeException("Error when selecting the departure point: " + stationName, e);
        }
        return this;
    }

    @Step("Выбор пункта прибытия: {stationName}")
    public MainPage enterArrivalStation(String stationName) {
        LOGGER.info("Entering an arrival point: {}", stationName);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(arrivalStationInput)).click();
            arrivalStationInput.clear();
            arrivalStationInput.sendKeys(stationName);
            Thread.sleep(1000);
            arrivalStationInput.sendKeys(Keys.ENTER);
            LOGGER.info("Arrival point '{}' selected.", stationName);
        } catch (Exception e) {
            LOGGER.error("Error when selecting the arrival point '{}': {}", stationName, e.getMessage());
            throw new RuntimeException("Error when selecting the arrival point: " + stationName, e);
        }
        return this;
    }

    @Step("Выбор двух взрослых пассажиров")
    public MainPage setTwoAdultPassenger() {
        LOGGER.info("The choice of two adult passengers.");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(travelersButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(incrementAdultPassengerCounter)).click();
            wait.until(ExpectedConditions.elementToBeClickable(incrementAdultPassengerCounter)).click();
            wait.until(ExpectedConditions.elementToBeClickable(backgroundDiv)).click();
            LOGGER.info("Two adult passengers selected.");
        } catch (Exception e) {
            LOGGER.error("Error when setting the number of passengers: {}", e.getMessage());
            throw new RuntimeException("Error when setting the number of passengers", e);
        }
        return this;
    }

    @Step("Установка даты в календаре (5 ноября 2025)")
    public MainPage setFifthOfNovemberDate() {
        LOGGER.info("Setting the date (November 5, 2025).");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(calendarButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(calendarNextMonthButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(dateDiv)).click();
            LOGGER.info("November 5, 2025 selected.");
        } catch (Exception e) {
            LOGGER.error("Error when setting the date (November 5, 2025): {}", e.getMessage());
            throw new RuntimeException("Error when setting the date (November 5, 2025)", e);
        }
        return this;
    }

    @Step("Нажатие на кнопку 'Search trains' и переключение на новую вкладку (расписания поездов)")
    public TimetablePage searchButtonClick() {
        LOGGER.info("Clicking on the 'Search trains' button");
        String originalWindow = driver.getWindowHandle();

        try {
            int initialWindowCount = driver.getWindowHandles().size();
            LOGGER.info("Number of windows before a click: {}", initialWindowCount);

            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
            button.click();
            LOGGER.info("The 'Search trains' button has been clicked.");

            LOGGER.info("Waiting for a new tab to open...");
            wait.until(ExpectedConditions.numberOfWindowsToBe(initialWindowCount + 1));

            Set<String> allWindows = driver.getWindowHandles();
            String newWindowHandle = null;
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(originalWindow)) {
                    newWindowHandle = windowHandle;
                    break;
                }
            }
            if (newWindowHandle == null) {
                throw new RuntimeException("Couldn't find a new tab");
            }

            driver.switchTo().window(newWindowHandle);
            LOGGER.info("Switching to a new tab is done. Handle: {}", newWindowHandle);
            WebDriverWait extendedWait = new WebDriverWait(driver, EXPLICIT_WAIT_25);
            extendedWait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains(TIMETABLE_PAGE),
                    ExpectedConditions.urlContains(TIMETABLE_V9_PAGE)
            ));
            String currentUrl = driver.getCurrentUrl();
            LOGGER.info("Current URL of the new tab: {}", currentUrl);

            if (currentUrl != null && currentUrl.contains(TIMETABLE_V9_PAGE)) {
                LOGGER.info("Creating a TimetableV9Page object for a URL: {}", currentUrl);
                return new TimetableV9Page(driver);
            } else if (currentUrl != null && currentUrl.contains(TIMETABLE_PAGE)) {
                LOGGER.info("Creating a TimetableLegacyPage object for a URL: {}", currentUrl);
                return new TimetableLegacyPage(driver);
            } else {
                LOGGER.error("Unknown URL: {}", currentUrl);
                throw new RuntimeException("Unknown URL: " + currentUrl);
            }

        } catch (TimeoutException e) {
            LOGGER.error("Timeout while waiting for a new tab or URL to load: {}", e.getMessage());
            LOGGER.info("Current open tabs: {}", driver.getWindowHandles().size());
            throw new RuntimeException("Timeout when switching to a new tab", e);
        } catch (Exception e) {
            LOGGER.error("Error when clicking on the 'Search trains' button or switching to a new tab: {}", e.getMessage());
            throw new RuntimeException("Error when clicking on the 'Search trains' button or switching to a new tab", e);
        }
    }
}