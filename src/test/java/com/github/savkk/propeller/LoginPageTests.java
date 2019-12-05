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
    private static final int WEBDRIVER_WAIT_TIMEOUT = timeOutsConfig.webDriverWait();
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
        loginPageSteps.fillField("Your Login", "foo");
        loginPageSteps.fillField("Your Password", "bar");
        loginPageSteps.clickSignIn();
        loginPageSteps.acceptAlert("Are you sure you want to login?", WEBDRIVER_WAIT_TIMEOUT);
        loginPageSteps.acceptAlert("Really sure?", timeOutsConfig.webDriverWait());
        loginPageSteps.acceptAlert("Authentication failed. Please re-enter your login credentials",
                timeOutsConfig.webDriverWait());
        Assert.assertTrue(loginPageSteps.isLoaded());
    }
}
