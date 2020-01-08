package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.config.TimeOutsConfig;
import com.github.savkk.propeller.pages.BasePage;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.atlas.core.Atlas;
import io.qameta.atlas.webdriver.WebDriverConfiguration;
import org.aeonbits.owner.ConfigFactory;
import org.awaitility.Durations;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Cookie;
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
import static ru.yandex.qatools.matchers.webdriver.DisplayedMatcher.displayed;

abstract class BasePageSteps<Page extends BasePage> {
    private static final Logger log = LoggerFactory.getLogger(BasePageSteps.class);
    private final WebDriver webDriver;
    private final Atlas atlas;
    protected static final TimeOutsConfig TIMEOUTS_CONFIG = ConfigFactory.create(TimeOutsConfig.class);
    protected static final int WEBDRIVER_WAIT_TIMEOUT = TIMEOUTS_CONFIG.webDriverWait();

    protected BasePageSteps(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.atlas = new Atlas(new WebDriverConfiguration(webDriver));
    }

    private WebDriverWait getWebDriverWait(WebDriver webDriver, int timeout) {
        return new WebDriverWait(webDriver, timeout);
    }

    protected Page open(Class<Page> page) {
        return atlas.create(webDriver, page);
    }

    protected WebDriver getWebDriver() {
        return webDriver;
    }

    @Step("Подтвердить высплывающиее окно с текстом - {expectedAlertText}")
    public <T extends BasePageSteps> T acceptAlert(String expectedAlertText, int timeout) {
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

    @Step("Навести курсор на элемент {element}")
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
    public String getTextFromFile(Path filePath) {
        await("Не удалось прочитать файла " + filePath.toString())
                .timeout(Durations.ONE_MINUTE)
                .pollDelay(Durations.TWO_SECONDS)
                .until(() -> Files.isReadable(filePath));
        log.info("Получить текст из файла {}", filePath);
        try {
            String text = new String(Files.readAllBytes(filePath));
            Allure.addAttachment("Текст", text);
            return text;
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать файл " + filePath.toString());
        }
    }

    @Step("Получить куки {cookieKey}")
    public String getCookieValue(String cookieKey) {
        log.info("Получить куки {}", cookieKey);
        Cookie cookieNamed = getWebDriver().manage().getCookieNamed(cookieKey);
        log.info("Значение куки - {}", cookieNamed);
        Allure.addAttachment("Значение куки", String.valueOf(cookieNamed));
        return cookieNamed.getValue();
    }

    @Step("Заполнить поле '{fieldTitle}' значением '{value}'")
    public <T extends BasePageSteps> T fillField(String fieldTitle, String value) {
        log.info("Заполнить поле '{}' значением '{}'", fieldTitle, value);
        onPage().field(fieldTitle).sendKeys(value);
        return (T) this;
    }

    @Step("Очистить поле '{fieldTitle}'")
    public <T extends BasePageSteps> T clearField(String fieldTitle) {
        log.info("Очистить поле '{}'", fieldTitle);
        onPage().field(fieldTitle).clear();
        return (T) this;
    }

    @Step("Получить значение из поля '{fieldTitle}'")
    public String getFieldValue(String fieldTitle) {
        log.info("Получить значение из поля {}", fieldTitle);
        String text = onPage().field(fieldTitle).getAttribute("value");
        log.info("Значение - {}", text);
        return text;
    }

    @Step("Кликнуть по кнопке {buttonTitle}")
    public <T extends BasePageSteps> T clickButton(String buttonTitle) {
        log.info("Кликнуть по кнопке {}", buttonTitle);
        onPage().button(buttonTitle).waitUntil("Кнопка " + buttonTitle + " не отобразилась", displayed(), WEBDRIVER_WAIT_TIMEOUT)
                .click();
        return (T) this;
    }

    @Step("Кнопка 'buttonTitle' активна")
    public boolean buttonIsEnabled(String buttonTitle) {
        boolean enabled = onPage().button(buttonTitle)
                .waitUntil("Кнопка " + buttonTitle + " не отобразилась", displayed(), WEBDRIVER_WAIT_TIMEOUT)
                .isEnabled();
        Allure.addAttachment("Активна", enabled ? "да" : "нет");
        return enabled;
    }

    @Step("Обновить страницу")
    public void refresh() {
        getWebDriver().navigate().refresh();
    }

    protected abstract Page onPage();

    @Step("Страница загружена")
    public abstract boolean isLoaded();
}
