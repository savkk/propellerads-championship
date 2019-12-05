package com.github.savkk.propeller;

import com.github.savkk.propeller.config.CredentialsConfig;
import com.github.savkk.propeller.steps.ArticlesPageSteps;
import com.github.savkk.propeller.steps.LoginPageSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.testng.annotations.Test;

@Feature("Вход в систему")
public class LoginPageTests extends PropellerAdsTests {
    private CredentialsConfig credentialsConfig = ConfigFactory.create(CredentialsConfig.class);

    @Test
    @Story("Вход с валидными данными")
    public void validCreds() {
        LoginPageSteps loginPageSteps = new LoginPageSteps(getWebDriver());
        loginPageSteps.isLoaded();
        loginPageSteps.signIn(credentialsConfig.login(), credentialsConfig.password());
        Assert.assertTrue(new ArticlesPageSteps(getWebDriver()).isLoaded());
    }

    @Test
    @Story("Вход с невалидными данными")
    public void invalidCreds() {
        LoginPageSteps loginPageSteps = new LoginPageSteps(getWebDriver());
        loginPageSteps.isLoaded();
        loginPageSteps.signIn("foo", "bar");
        loginPageSteps.acceptAlert("Authentication failed. Please re-enter your login credentials",
                WEBDRIVER_WAIT_TIMEOUT);
        Assert.assertTrue(loginPageSteps.isLoaded());
    }
}
