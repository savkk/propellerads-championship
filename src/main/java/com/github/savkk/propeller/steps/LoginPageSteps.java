package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.LoginPage;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ru.yandex.qatools.matchers.webdriver.DisplayedMatcher.displayed;

public final class LoginPageSteps extends BasePageSteps<LoginPage> {
    private static final Logger log = LoggerFactory.getLogger(LoginPageSteps.class);

    @Step("Войти в систему под - '{user}'/'{password}'")
    public void signIn(String user, String password) {
        log.info("Войти в систему под пользователем - '{}'/'{}'", user, password);
        fillField("Your Login", user);
        fillField("Your Password", password);
        clickSignIn();
        acceptAlert("Are you sure you want to login?", TIMEOUTS_CONFIG.webDriverWait());
        acceptAlert("Really sure?", TIMEOUTS_CONFIG.webDriverWait());
    }

    @Step("Активировать и заполнить поле '{fieldTitle}' значением '{value}'")
    @Override
    public LoginPageSteps fillField(String fieldTitle, String value) {
        log.info("Активировать и заполнить поле '{}' значением '{}'", fieldTitle, value);
        onPage().field(fieldTitle).activator().click();
        onPage().field(fieldTitle).sendKeys(value);
        return this;
    }

    @Step("Подождать появление кнопки 'Sign In' и нажать на неё")
    public LoginPageSteps clickSignIn() {
        log.info("Подождать появление кнопки 'Sign In' и нажать на неё");
        moveToElement(onPage().button("Hover me faster!"), "Навести курсор на кнопку 'Hover me faster!'");
        onPage().signInButton()
                .waitUntil("Кнопка Sign In не отобразилась", displayed(), TIMEOUTS_CONFIG.webDriverWait())
                .click();
        return this;
    }


    public LoginPageSteps(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected LoginPage onPage() {
        return open(LoginPage.class);
    }

    @Override
    @Step("Страница Login загружена")
    public boolean isLoaded() {
        try {
            return onPage().title().isDisplayed() &&
                    onPage().field("Your Login").isDisplayed() &&
                    onPage().field("Your Password").isDisplayed();
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Step("Войти в систему используя куки")
    public void signInByCookie(String secretKey, String secretValue) {
        Cookie cookie = new Cookie(secretKey, secretValue);
        getWebDriver().manage().addCookie(cookie);
        getWebDriver().navigate().refresh();
    }
}
