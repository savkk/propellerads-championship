package com.github.savkk.propeller.tests;

import com.github.savkk.propeller.fixtures.Fixtures;
import com.github.savkk.propeller.steps.ArticlesPageSteps;
import com.github.savkk.propeller.steps.LoginPageSteps;
import com.github.savkk.propeller.steps.ProfilePageSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.savkk.propeller.constants.ErrorMessages.*;

@Feature("Страница профиля пользователя")
public class ProfilePageTests extends Fixtures {


    @BeforeMethod(description = "Вход в систему и переход на страницу профиля")
    public void login() {
        LoginPageSteps loginPageSteps = new LoginPageSteps(getWebDriver());
        Assert.assertTrue("Страница логина не загрузилась", loginPageSteps.isLoaded());
        loginPageSteps.signInByCookie(credentialsConfig.cookieSecretKey(), credentialsConfig.cookieSecretValue());
        new ArticlesPageSteps(getWebDriver()).openProfile();
    }

    @Story("Проверка сохранения значения полей First Name и Last Name")
    @Test
    public void saveNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        String firstName = "Stephen";
        String lastName = "Hawking";
        profilePageSteps.fillField("First name", firstName);
        profilePageSteps.fillField("Last name", lastName);
        profilePageSteps.clickButton("Save user info");
        Assert.assertEquals(firstName, profilePageSteps.getCookieValue("firstName"));
        Assert.assertEquals(lastName, profilePageSteps.getCookieValue("lastName"));
        profilePageSteps.refresh();
        Assert.assertEquals(firstName, profilePageSteps.getFieldValue("First name"));
        Assert.assertEquals(lastName, profilePageSteps.getFieldValue("Last name"));
    }

    @Story("Проверка появления ошибки при заполнении только имени")
    @Test
    public void fillOnlyFirstNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.fillField("First name", "Stephen");
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
        Assert.assertFalse(INVALID_FEEDBACK_IS_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
    }

    @Story("Проверка появления ошибки при заполнении только фамилии")
    @Test
    public void fillOnlyLastNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.fillField("Last name", "Hawking");
        profilePageSteps.clickButton("Save user info");
        Assert.assertFalse(INVALID_FEEDBACK_IS_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
    }

    @Story("Проверка исчезновения ошибки после заполнения поля First Name")
    @Test
    public void fillAfterErrorFirstNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
        profilePageSteps.fillField("First name", "Stephen");
        profilePageSteps.clickButton("Save user info");
        Assert.assertFalse(INVALID_FEEDBACK_IS_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
    }


    @Story("Проверка исчезновения ошибки после заполнения поля Last Name")
    @Test
    public void fillAfterErrorLastNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.fillField("First name", "Stephen");
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
        profilePageSteps.clearField("First name");
        profilePageSteps.fillField("Last name", "Hawking");
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
        Assert.assertFalse(INVALID_FEEDBACK_IS_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
    }

    @Story("Проверка появления ошибки при пустых значениях фамилии и имени")
    @Test
    public void noFillNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
    }

    @Story("Проверка сохранения платежной информации")
    @Test
    public void savePaymentInfo() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.clickMenu("Payment info");
        String cardNumber = "1234 1234 1234 1234";
        profilePageSteps.fillField("Card Number", cardNumber);
        String paymentSystem = "Visa";
        profilePageSteps.selectPaymentSystem(paymentSystem);
        profilePageSteps.selectPaymentDay(5);
        profilePageSteps.clickButton("Save payment info");
        Assert.assertEquals(cardNumber, profilePageSteps.getCookieValue("cardNumber"));
        Assert.assertEquals("1", profilePageSteps.getCookieValue("paymentSystem"));
        Assert.assertEquals("5", profilePageSteps.getCookieValue("paymentDay"));
        profilePageSteps.refresh();
        profilePageSteps.clickMenu("Payment info");
        Assert.assertEquals(cardNumber, profilePageSteps.getFieldValue("Card Number"));
        Assert.assertEquals(paymentSystem, profilePageSteps.getPaymentSystem());
        Assert.assertEquals("Current value: 5", profilePageSteps.getDayOfPayment());
    }

    @Story("Проверка появления ошибки при сохранении пустых полей о платежной информации")
    @Test
    public void saveInvalidPaymentInfo() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.clickMenu("Payment info");
        profilePageSteps.clickButton("Save payment info");
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your card number."));
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please select your payment system."));
    }

    @Story("Проверка появления ошибки при сохранении пустого поля с информацией о платежной системе")
    @Test
    public void saveInvalidPaymentSystemInfoTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.clickMenu("Payment info");
        profilePageSteps.fillField("Card Number", "1234 1234 1234 1234");
        profilePageSteps.clickButton("Save payment info");
        Assert.assertFalse(INVALID_FEEDBACK_IS_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your card number."));
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please select your payment system."));
    }

    @Story("Проверка исчезновения ошибки после заполнения поля Card Number")
    @Test
    public void fillAfterErrorCardNumberTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.clickMenu("Payment info");
        profilePageSteps.clickButton("Save payment info");
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your card number."));
        profilePageSteps.fillField("Card Number", "1234 1234 1234 1234");
        profilePageSteps.clickButton("Save payment info");
        Assert.assertFalse(INVALID_FEEDBACK_IS_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please set your card number."));
    }

    @Story("Проверка исчезновения ошибки после заполнения поля с информацией о платежной системе")
    @Test
    public void fillAfterErrorPaymentSystemInfoTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, profilePageSteps.isLoaded());
        profilePageSteps.clickMenu("Payment info");
        profilePageSteps.fillField("Card Number", "1234 1234 1234 1234");
        profilePageSteps.clickButton("Save payment info");
        Assert.assertTrue(INVALID_FEEDBACK_IS_NOT_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please select your payment system."));
        profilePageSteps.clearField("Card Number");
        profilePageSteps.selectPaymentSystem("Visa");
        profilePageSteps.clickButton("Save payment info");
        Assert.assertFalse(INVALID_FEEDBACK_IS_DISPLAYED,
                profilePageSteps.invalidFeedbackIsDisplayed("Please select your payment system."));
    }

}
