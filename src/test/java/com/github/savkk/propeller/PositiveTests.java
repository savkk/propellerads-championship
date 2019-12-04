package com.github.savkk.propeller;

import com.github.savkk.propeller.config.CredentialsConfig;
import com.github.savkk.propeller.steps.LoginPageSteps;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.Test;

public class PositiveTests extends PropellerAdsTests {
    private CredentialsConfig credentialsConfig = ConfigFactory.create(CredentialsConfig.class);

    @Test
    public void first() {
        LoginPageSteps loginPageSteps = new LoginPageSteps(getWebDriver());
        loginPageSteps.login(credentialsConfig.login(), credentialsConfig.password());
    }
}
