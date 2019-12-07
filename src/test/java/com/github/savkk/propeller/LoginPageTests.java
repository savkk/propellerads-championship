package com.github.savkk.propeller;

import com.github.savkk.propeller.steps.ArticlesPageSteps;
import com.github.savkk.propeller.steps.LoginPageSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Assert;
import org.testng.annotations.Test;

@Feature("Страница логина")
public class LoginPageTests extends Fixtures {

    @Test
    @Story("Вход с валидными данными")
    public void validCreds() {
        LoginPageSteps loginPageSteps = new LoginPageSteps(getWebDriver());
        Assert.assertTrue("Страница логина не загрузилась", loginPageSteps.isLoaded());
        loginPageSteps.signIn(credentialsConfig.login(), credentialsConfig.password());
        Assert.assertTrue(new ArticlesPageSteps(getWebDriver()).isLoaded());
    }

    @Test
    @Story("Вход с невалидными данными")
    public void invalidCreds() {
        LoginPageSteps loginPageSteps = new LoginPageSteps(getWebDriver());
        Assert.assertTrue("Страница логина не загрузилась", loginPageSteps.isLoaded());
        loginPageSteps.signIn("foo", "bar");
        loginPageSteps.acceptAlert("Authentication failed. Please re-enter your login credentials",
                WEBDRIVER_WAIT_TIMEOUT);
        Assert.assertTrue("Страница логина не загрузилась", loginPageSteps.isLoaded());
    }
}
