package ninja.rail.pages;


import io.qameta.allure.Step;
import ninja.rail.core.BaseSeleniumPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Set;

import static ninja.rail.constants.Constant.Urls.TIMETABLE_PAGE;

/**
 * Page Object для страницы ввода данных пассажиров
 * Содержит форму заполнения информации о пассажирах
 */
public class PassengerDetailsPage extends BaseSeleniumPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PassengerDetailsPage.class);
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    @FindBy(xpath = "//*[@id='checkout-passengers-form_passengersCategories_adult_0_full_name']")
    private WebElement nameAsInPassportInput;

    @FindBy(xpath = "//span[contains(@class, 'jOLkZL')]")
    private WebElement reflectionHeader;

    public PassengerDetailsPage(WebDriver driver) {
        super(driver);
        this.pagePath = "";
        PageFactory.initElements(driver, this);
    }

    public String getReflectionHeaderText() {
        return reflectionHeader.getText();
    }

    @Step("Вставка имени пассажира в окно ввода: {name}")
    public PassengerDetailsPage setNameAsInPassportInput(String name) {
        nameAsInPassportInput.clear();
        nameAsInPassportInput.sendKeys(name);
        nameAsInPassportInput.sendKeys(Keys.ENTER);
        return this;
    }

    @Step("Переключение на страницу расписания поездов")
    public TimetablePage switchToTimetablePage() {
        LOGGER.info("Switching to timetable page: {}", TIMETABLE_PAGE);
        try {
            String originalWindow = driver.getWindowHandle();
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            Set<String> allWindows = driver.getWindowHandles();
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            wait.until(ExpectedConditions.urlContains(TIMETABLE_PAGE));
            LOGGER.info("Successfully switched to timetable page. Current URL: {}", driver.getCurrentUrl());
            return new TimetablePage(driver);
        } catch (Exception e) {
            LOGGER.error("Failed to switch to timetable page", e);
            throw new RuntimeException("Failed to switch to timetable page: " + TIMETABLE_PAGE, e);
        }
    }


}