package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.ProfilePage;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ru.yandex.qatools.matchers.webdriver.DisplayedMatcher.displayed;

public final class ProfilePageSteps extends BasePageSteps<ProfilePage> {
    private static final Logger log = LoggerFactory.getLogger(ProfilePageSteps.class);

    public ProfilePageSteps(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected ProfilePage onPage() {
        return open(ProfilePage.class);
    }

    @Override
    @Step("Страница Profile загружена")
    public boolean isLoaded() {
        return onPage().button("Save user info").isDisplayed()
                && onPage().field("First name").isDisplayed()
                && onPage().field("Last name").isDisplayed()
                && onPage().menuButton("User Profile").isDisplayed()
                && onPage().menuButton("Payment info").isDisplayed();
    }

    @Step("Отображается сообщение об ошибке - {expectedMessage}")
    public boolean invalidFeedbackIsDisplayed(String expectedMessage) {
        log.info("Отображается сообщение об ошибке - {}", expectedMessage);
        boolean displayed = onPage().invalidFeedback(expectedMessage).isDisplayed();
        log.info("{}", displayed);
        return displayed;
    }

    @Step("Кликнуть по пункту меню {menuTitle}")
    public ProfilePageSteps clickMenu(String menuTitle) {
        log.info("Кликнуть по пункту меню {}", menuTitle);
        onPage().menuButton(menuTitle).waitUntil(displayed(), WEBDRIVER_WAIT_TIMEOUT).click();
        return this;
    }

    @Step("Выбрать платежную систему {paymentSystem}")
    public ProfilePageSteps selectPaymentSystem(String paymentSystem) {
        log.info("Выбрать платежную систему {}", paymentSystem);
        Select webElement = new Select(onPage().paymentSystemSelect().waitUntil(displayed(), WEBDRIVER_WAIT_TIMEOUT));
        webElement.selectByVisibleText(paymentSystem);
        return this;
    }

    @Step("Получить текущую платежную систему")
    public String getPaymentSystem() {
        log.info("Получить текущую платежную систему");
        String text = new Select(onPage().paymentSystemSelect().waitUntil(displayed(), WEBDRIVER_WAIT_TIMEOUT))
                .getFirstSelectedOption()
                .getText();
        log.info("{}", text);
        return text;
    }

    @Step("Получить текущий день платежа")
    public String getDayOfPayment() {
        log.info("Получить текущий день платежа");
        String text = onPage().dayOfPaymentBlock().currentValue().getText();
        log.info("{}", text);
        return text;
    }

    @Step("Выбрать день платежа {day}")
    public void selectPaymentDay(int day) {
        log.info("Выбрать день платежа {}", day);
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].value = arguments[1];",
                onPage().dayOfPaymentBlock().inputDayRange().waitUntil(displayed(), WEBDRIVER_WAIT_TIMEOUT),
                day);
    }
}
