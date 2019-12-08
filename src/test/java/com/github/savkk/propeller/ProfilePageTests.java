package com.github.savkk.propeller;

import com.github.savkk.propeller.steps.ArticlesPageSteps;
import com.github.savkk.propeller.steps.LoginPageSteps;
import com.github.savkk.propeller.steps.ProfilePageSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
        Assert.assertTrue("Страница профиля пользователя не загрузилась полностью", profilePageSteps.isLoaded());
        String firstName = "Stephen";
        String lastName = "Hawking";
        profilePageSteps.fillField("First name", firstName);
        profilePageSteps.fillField("Last name", lastName);
        profilePageSteps.clickButton("Save user info");
        Assert.assertEquals(firstName, profilePageSteps.getCookie("firstName").getValue());
        Assert.assertEquals(lastName, profilePageSteps.getCookie("lastName").getValue());
        profilePageSteps.getWebDriver().navigate().refresh();
        Assert.assertEquals(firstName, profilePageSteps.getFieldValue("First name"));
        Assert.assertEquals(lastName, profilePageSteps.getFieldValue("Last name"));
    }

    @Story("Проверка появления ошибки при заполнении только имени")
    @Test
    public void fillOnlyFirstNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue("Страница профиля пользователя не загрузилась полностью", profilePageSteps.isLoaded());
        profilePageSteps.fillField("First name", "Stephen");
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
        Assert.assertFalse(profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
    }

    @Story("Проверка появления ошибки при заполнении только фамилии")
    @Test
    public void fillOnlyLastNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue("Страница профиля пользователя не загрузилась полностью", profilePageSteps.isLoaded());
        profilePageSteps.fillField("Last name", "Hawking");
        profilePageSteps.clickButton("Save user info");
        Assert.assertFalse(profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
        Assert.assertTrue(profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
    }

    @Story("Проверка исчезновения ошибки после заполнения поля First Name")
    @Test
    public void fillAfterErrorFirstNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue("Страница профиля пользователя не загрузилась полностью", profilePageSteps.isLoaded());
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
        profilePageSteps.fillField("First name", "Stephen");
        profilePageSteps.clickButton("Save user info");
        Assert.assertFalse(profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
    }


    @Story("Проверка исчезновения ошибки после заполнения поля Last Name")
    @Test
    public void fillAfterErrorLastNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue("Страница профиля пользователя не загрузилась полностью", profilePageSteps.isLoaded());
        profilePageSteps.fillField("First name", "Stephen");
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
        profilePageSteps.clearField("First name");
        profilePageSteps.fillField("Last name", "Hawking");
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
        Assert.assertFalse(profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
    }

    @Story("Проверка появления ошибки при пустых значениях фамилии и имени")
    @Test
    public void noFillNameTest() {
        ProfilePageSteps profilePageSteps = new ProfilePageSteps(getWebDriver());
        Assert.assertTrue("Страница профиля пользователя не загрузилась полностью", profilePageSteps.isLoaded());
        profilePageSteps.clickButton("Save user info");
        Assert.assertTrue(profilePageSteps.invalidFeedbackIsDisplayed("Please set your last name."));
        Assert.assertTrue(profilePageSteps.invalidFeedbackIsDisplayed("Please set your first name."));
    }


}
