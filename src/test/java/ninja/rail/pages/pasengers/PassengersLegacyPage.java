package ninja.rail.pages.pasengers;

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

import static ninja.rail.constants.Constant.TimeoutVariable.EXPLICIT_WAIT;

public class PassengersLegacyPage extends BaseSeleniumPage implements PassengersPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PassengersLegacyPage.class);
    private final WebDriverWait wait;

    @FindBy(xpath = "//span[@class='sc-cf59c1f-1 dfrRB']")
    private WebElement header;

    @FindBy(xpath = "//*[@id='checkout-passengers-form_passengersCategories_adult_0_full_name']")
    private WebElement passengerName;

    public PassengersLegacyPage(WebDriver driver) {
        super(driver);
        this.pagePath = "";
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, EXPLICIT_WAIT);
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
        return null;
    }

    @Override
    public PassengersPage selectFirstCitizenshipInLists() {
        return null;
    }

    @Override
    public PassengersPage enterGender(String gender) {
        return null;
    }

    @Override
    public PassengersPage enterBirthdayDate(String birthdayDate) {
        return null;
    }

    @Override
    public String getAgeNotify() {
        return "";
    }
}
