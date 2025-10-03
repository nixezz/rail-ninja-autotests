package ninja.rail.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static ninja.rail.constants.Constant.TimeoutVariable.IMPLICIT_WAIT;
import static ninja.rail.constants.Constant.TimeoutVariable.PAGE_LOAD_TIMEOUT;

@ExtendWith(TestListener.class)
public abstract class BaseSeleniumTest {
    protected WebDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSeleniumTest.class);

    @BeforeEach
    public void setUp() {
        LOGGER.info("Starting setUp procedure..");
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--disable-gpu");
        driver = new ChromeDriver(chromeOptions);
        LOGGER.info("WebDriver initialized: " + (driver != null));
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        LOGGER.info("SetUp completed.");
    }

}