package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.config.TimeOutsConfig;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.atlas.core.Atlas;
import io.qameta.atlas.webdriver.WebDriverConfiguration;
import org.aeonbits.owner.ConfigFactory;
import org.awaitility.Durations;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.awaitility.Awaitility.await;

abstract class PageSteps<T> {
    private static final Logger log = LoggerFactory.getLogger(PageSteps.class);
    private final WebDriver webDriver;
    private final Atlas atlas;
    protected static final TimeOutsConfig TIMEOUTS_CONFIG = ConfigFactory.create(TimeOutsConfig.class);
    protected static final int WEBDRIVER_WAIT_TIMEOUT = TIMEOUTS_CONFIG.webDriverWait();

    protected PageSteps(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.atlas = new Atlas(new WebDriverConfiguration(webDriver));
    }

    private WebDriverWait getWebDriverWait(WebDriver webDriver, int timeout) {
        return new WebDriverWait(webDriver, timeout);
    }

    protected T open(Class<T> page) {
        return atlas.create(webDriver, page);
    }

    protected abstract T $();

    public WebDriver getWebDriver() {
        return webDriver;
    }

    @Step("Подтвердить высплывающиее окно с текстом - {expectedAlertText}")
    public T acceptAlert(String expectedAlertText, int timeout) {
        log.info("Подтвердить высплывающиее окно с текстом - {}", expectedAlertText);
        getWebDriverWait(webDriver, timeout)
                .withMessage("Всплывающиее окно с текстом " + expectedAlertText + " не появилось")
                .until(driver -> {
                    Alert alert = driver.switchTo().alert();
                    if (!alert.getText().equals(expectedAlertText)) {
                        return false;
                    }
                    alert.accept();
                    return true;
                });
        return (T) this;
    }

    public void moveToElement(WebElement element) {
        new Actions(getWebDriver())
                .moveToElement(element)
                .perform();
    }

    @Step("{stepDescription}")
    public void moveToElement(WebElement element, String stepDescription) {
        log.info(stepDescription);
        moveToElement(element);
    }

    @Step("Получить текст из файла {filePath}")
    @Attachment("Текст")
    public String getTextFromFile(Path filePath) {
        await("Не удалось прочитать файла " + filePath.toString())
                .timeout(Durations.ONE_MINUTE)
                .pollDelay(Durations.TWO_SECONDS)
                .until(() -> Files.isReadable(filePath));
        log.info("Получить текст из файла {}", filePath);
        try {
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать файл " + filePath.toString());
        }
    }

    public abstract boolean isLoaded();
}
