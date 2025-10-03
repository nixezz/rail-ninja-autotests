package ninja.rail.pages.timetable;

import io.qameta.allure.Step;
import ninja.rail.core.BaseSeleniumPage;
import ninja.rail.pages.pasengers.PassengersLegacyPage;
import ninja.rail.pages.pasengers.PassengersPage;
import ninja.rail.pages.pasengers.PassengersV9Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ninja.rail.constants.Constant.TimeoutVariable.EXPLICIT_WAIT;
import static ninja.rail.constants.Constant.TimeoutVariable.EXPLICIT_WAIT_25;
import static ninja.rail.constants.Constant.Urls.PASSENGERS_FORM_PAGE;
import static ninja.rail.constants.Constant.Urls.PASSENGERS_FORM_V9_PAGE;

public class TimetableLegacyPage extends BaseSeleniumPage implements TimetablePage {
    private final WebDriverWait wait;
    private static final Logger LOGGER = LoggerFactory.getLogger(TimetableLegacyPage.class);

    public TimetableLegacyPage(WebDriver driver) {
        super(driver);
        this.pagePath = "";
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, EXPLICIT_WAIT);
    }

    @Override
    @Step("Выбор первого поезда в расписании")
    public TimetableLegacyPage selectFirstTrain() {
        LOGGER.info("Selecting the first train in the list");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.sc-87009a5e-2.bkprhe")));
        WebElement firstTrain = driver.findElement(By.xpath("/html/body/div[1]/div/div/main/div[2]/div[2]/div[1]/div/div"));
        firstTrain.click();
        return this;
    }

    @Override
    @Step("Нажатие на кнопку 'Book' в окне Flexible - выбор билета")
    public PassengersPage clickButtonToPassengerInfo(){
        LOGGER.info("Clicking on the Continue button");

        try {
            String currentUrl = driver.getCurrentUrl();
            LOGGER.info("The current URL before the click: {}", currentUrl);
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"__next\"]/div/div/main/div[2]/div[2]/div[1]/div[2]/div[2]/div[2]/button")
            ));
            button.click();
            LOGGER.info("The click on the Continue button is completed");

            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));

            WebDriverWait extendedWait = new WebDriverWait(driver, EXPLICIT_WAIT_25);
            extendedWait.until(ExpectedConditions.or(
                    ExpectedConditions.urlMatches(".*" + PASSENGERS_FORM_PAGE.replace("{}", "[^/]+") + ".*"),
                    ExpectedConditions.urlMatches(".*" + PASSENGERS_FORM_V9_PAGE.replace("{}", "[^/]+") + ".*")
            ));

            String newUrl = driver.getCurrentUrl();
            LOGGER.info("The transition is completed. New URL: {}", newUrl);

            if (newUrl.contains("/v9/trains/order/") && newUrl.contains("/passenger")) {
                LOGGER.info("Creating a PassengersV9Page object for a URL: {}", newUrl);
                return new PassengersV9Page(driver);
            } else if (newUrl.contains("/trains/order/") && newUrl.contains("/passenger")) {
                LOGGER.info("Creating a PassengerLegacyPage object for a URL: {}", newUrl);
                return new PassengersLegacyPage(driver);
            } else {
                LOGGER.error("Unknown passenger page URL: {}", newUrl);
                throw new RuntimeException("Unknown passenger page URL: " + newUrl);
            }

        } catch (TimeoutException e) {
            LOGGER.error("Timeout while waiting for a user to go to the passenger page: {}", e.getMessage());
            LOGGER.info("Current URL: {}", driver.getCurrentUrl());
            throw new RuntimeException("Timeout while waiting for a user to go to the passenger page", e);
        } catch (Exception e) {
            LOGGER.error("Error when going to the passengers page: {}", e.getMessage());
            throw new RuntimeException("Error when going to the passengers page", e);
        }

    }

}