package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.config.TimeOutsConfig;
import io.qameta.allure.Step;
import io.qameta.atlas.core.Atlas;
import io.qameta.atlas.webdriver.WebDriverConfiguration;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class PageSteps<T> {
    private static final Logger log = LoggerFactory.getLogger(PageSteps.class);
    private final WebDriver webDriver;
    private final Atlas atlas;
    protected static final TimeOutsConfig timeOutsConfig = ConfigFactory.create(TimeOutsConfig.class);

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

    public abstract boolean isLoaded();
}
