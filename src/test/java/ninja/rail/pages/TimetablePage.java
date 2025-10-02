package ninja.rail.pages;

import io.qameta.allure.Step;
import ninja.rail.core.BaseSeleniumPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.Duration;
import java.util.List;

public class TimetablePage extends BaseSeleniumPage {
    private enum PageVersion {
        V9,  // "https://rail.ninja/v9/trains/order/timetable"
        LEGACY // "https://rail.ninja/trains/order/timetable"
    }

    private static final By bookButton = By.xpath("//*[@id=\"__next\"]/div/main/div[2]/div[3]/div[1]/div/div/div[2]/div[2]/button");

    private static final Logger LOGGER = LoggerFactory.getLogger(TimetablePage.class);

    public TimetablePage(WebDriver driver) {
        super(driver);
        this.pagePath = "";
        PageFactory.initElements(driver, this);
    }


    @Step("Клик на первый элемент расписания")
    public TimetablePage clickFirstScheduleItem() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> scheduleItems = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector("div.sc-cc91ad6f-1.ffjWTj div.sc-a5c55fa-0.dRqmGM")
                )
        );

        if (!scheduleItems.isEmpty()) {
            scheduleItems.get(0).click();
        } else {
            throw new AssertionError("Список расписания пуст");
        }
        return this;
    }


    @Step("Нажатие кнопки Continue")
    public void clickContinue() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(bookButton));
        button.click();
    }

    private void waitForElementVisible(WebElement element) throws InterruptedException {
        for (int i = 0; i < 30; i++) {
            if (element.isDisplayed()) {
                return;
            }
            Thread.sleep(500);
        }
        throw new TimeoutException("Element not visible: " + element);
    }

    private void waitForElementClickable(WebElement element) throws InterruptedException {
        for (int i = 0; i < 30; i++) {
            if (element.isEnabled() && element.isDisplayed()) {
                return;
            }
            Thread.sleep(500);
        }
        throw new TimeoutException("Element not clickable: " + element);
    }
}