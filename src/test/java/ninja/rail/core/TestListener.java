package ninja.rail.core;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TestListener implements TestWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void testSuccessful(ExtensionContext context) {
        WebDriver driver = getDriverFromContext(context);
        if(driver != null){
            driver.quit();
        }
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        WebDriver driver = getDriverFromContext(context);
        if(driver != null){
            driver.quit();
        }
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        WebDriver driver = getDriverFromContext(context);
        if(driver != null){
            driver.quit();
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver driver = getDriverFromContext(context);
        if (driver != null) {
            try {
                LOGGER.info("Test failed: {}. Taking screenshot...", context.getDisplayName());
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("screenshot-" + System.currentTimeMillis(), "image/png", "png", screenshot);
            } catch (Exception e) {
                LOGGER.error("Failed to take screenshot: {}", e.getMessage());
            }
        } else {
            LOGGER.warn("Driver is null, cannot take screenshot for test: {}", context.getDisplayName());
        }
        assert driver != null;
        driver.quit();
    }

    private WebDriver getDriverFromContext(ExtensionContext context) {
        return context.getRequiredTestInstance() instanceof BaseSeleniumTest ?
                ((BaseSeleniumTest) context.getRequiredTestInstance()).driver : null;
    }
}