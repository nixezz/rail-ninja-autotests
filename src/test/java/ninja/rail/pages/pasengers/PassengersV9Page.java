package ninja.rail.pages.pasengers;

import io.qameta.allure.Step;
import ninja.rail.core.BaseSeleniumPage;
import ninja.rail.utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ninja.rail.constants.Constant.TimeoutVariable.EXPLICIT_WAIT;

public class PassengersV9Page extends BaseSeleniumPage implements PassengersPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PassengersV9Page.class);
    private final WebDriverWait wait;
    private final Actions actions;

    @FindBy(xpath = "//span[contains(@class, 'jOLkZL')]")
    private WebElement header;

    @FindBy(xpath = "//*[@id='checkout-passengers-form_passengersCategories_adult_0_full_name']")
    private WebElement passengerName;

    public PassengersV9Page(WebDriver driver) {
        super(driver);
        this.pagePath = "";
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, EXPLICIT_WAIT);
        actions = new Actions(driver);
    }

    @Override
    @Step("Возвращение значения заголовка (Adult passenger 1)")
    public String getHeader() {
        LOGGER.info("Returning the text from the passenger's header (Adult passenger 1): {}", header.getText());
        return header.getText();
    }

    @Override
    @Step("Заполнение поля Name as in passport: {name}")
    public PassengersPage enterNameAsInPassport(String name) {
        LOGGER.info("Entering the passenger's name as in passport: {}", name);
        wait.until(ExpectedConditions.elementToBeClickable(passengerName)).click();
        passengerName.clear();
        passengerName.sendKeys(name);
        passengerName.sendKeys(Keys.ENTER);
        return this;
    }

    @Override
    public PassengersPage enterPassportNumber(String passportNumber) {
        return this;
    }

    @Override
    public PassengersPage selectFirstCitizenshipInLists() {
        return this;
    }

    @Override
    public PassengersPage enterGender(GenderEnum gender) {
        return this;
    }

    @Override
    @Step("Заполнение даты рождения пассажира: {dayOfBirth}")
    public PassengersPage enterBirthdayDate(String dayOfBirth) {

        LOGGER.info("Setting date of birth...");
        try {
            LOGGER.info("Parsing dateOfBirth: {}", dayOfBirth);
            int[] dayOfBirthArray = DateUtils.parseDate(dayOfBirth);
            int day = dayOfBirthArray[0], month = dayOfBirthArray[1], year = dayOfBirthArray[2];

            LOGGER.info("Setting day: {}", day);
            WebElement dayContainer = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='checkout-passengers-form_passengersCategories_adult_0_dob']/div[1]")
            ));
            actions.moveToElement(dayContainer).click().perform();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'ant-select-selector')]")
            ));
            WebElement dayInputElement = dayContainer.findElement(
                    By.cssSelector("input.ant-select-selection-search-input")
            );

            String inputId = dayInputElement.getAttribute("id");
            String prefix = inputId + "_list_";
            String dayIndex = Integer.toString(day - 1);
            String targetDayId = prefix + dayIndex;

            String currentActive = dayInputElement.getAttribute("aria-activedescendant");
            while (!currentActive.contains(targetDayId)) {
                actions.sendKeys(Keys.ARROW_DOWN).perform();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentActive = dayInputElement.getAttribute("aria-activedescendant");
            }
            actions.sendKeys(Keys.ENTER).perform();

            LOGGER.info("Setting month: {}", month);
            WebElement monthContainer = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='checkout-passengers-form_passengersCategories_adult_0_dob']/div[2]")
            ));
            actions.moveToElement(monthContainer).click().perform();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'ant-select-selector')]")
            ));
            WebElement monthInputElement = monthContainer.findElement(
                    By.cssSelector("input.ant-select-selection-search-input")
            );
            String monthInputId = monthInputElement.getAttribute("id");
            String monthPrefix = monthInputId + "_list_";
            String monthIndex = Integer.toString(month);
            String targetMonthId = monthPrefix + monthIndex;
            currentActive = monthInputElement.getAttribute("aria-activedescendant");
            while (!currentActive.equals(targetMonthId)) {
                actions.sendKeys(Keys.ARROW_DOWN).perform();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentActive = monthInputElement.getAttribute("aria-activedescendant");
            }
            actions.sendKeys(Keys.ENTER).perform();

            LOGGER.info("Setting year: {}", year);
            WebElement yearContainer = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='checkout-passengers-form_passengersCategories_adult_0_dob']/div[3]")
            ));
            actions.moveToElement(yearContainer).click().perform();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'ant-select-selector')]")
            ));
            WebElement yearInputElement = yearContainer.findElement(
                    By.cssSelector("input.ant-select-selection-search-input")
            );

            String yearInputId = yearInputElement.getAttribute("id");
            String yearPrefix = yearInputId + "_list_";
            String yearIndex = Integer.toString(2025 - year); // 2025 - id 0, 2024 - id 1 and etc.
            String targetYearId = yearPrefix + yearIndex;


            currentActive = yearInputElement.getAttribute("aria-activedescendant");
            while (!currentActive.equals(targetYearId)) {
                actions.sendKeys(Keys.ARROW_DOWN).perform();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentActive = yearInputElement.getAttribute("aria-activedescendant");
            }
            actions.sendKeys(Keys.ENTER).perform();
            LOGGER.info("Date of birth is set.");
        } catch (Exception e) {
            LOGGER.error("An error occurred when setting the date of birth of the Infant: {}", e.getMessage());
            throw new RuntimeException("An error occurred when setting the date of birth of the Infant: " + e);
        }
        return this;
    }

    @Override
    @Step("Возвращение значения заголовка возраста (для пассажиров младше 12 лет)")
    public String getAgeNotify() {
        LOGGER.info("Returning the age of the passenger's age, if they child or infant...");
        try{
            WebElement ageContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"checkout-passengers-form\"]/div[1]/div[3]/div[2]/div/div[1]/span[2]")));
            String text = ageContainer.getText();
            LOGGER.info("Returning text: {}", text);
            return text;
        } catch (Exception e) {
            LOGGER.warn("Text is not detected, returning empty string: {}", e.getMessage());
            return "";
        }
    }

    @Override
    @Step("Возвращение текста ошибки под полем ввода имени и фамилии пассажира")
    public String getIncorrectNameNotification() {
        LOGGER.info("Returning the error text under the passenger's first and last name field...");
        try {
            WebElement notificationContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"checkout-passengers-form_passengersCategories_adult_0_full_name_help\"]/div")));
            String text = notificationContainer.getText();
            LOGGER.info("Text detected: {}", text);
            return text;
        } catch (Exception e) {
            LOGGER.warn("Text is not detected, returning empty string: {}", e.getMessage());
            return "";
        }
    }
}
