package ninja.rail.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import static ninja.rail.constants.Constant.Urls.MAIN_PAGE;

public abstract class BaseSeleniumPage {
    protected static final String BASE_URL = MAIN_PAGE;
    protected WebDriver driver;
    protected String pagePath;

    public BaseSeleniumPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String url) {
        driver.get(url);
    }

    public void switchToWindow(int windowIndex) {
        driver.switchTo().window(driver.getWindowHandles().toArray()[windowIndex].toString());
    }
}