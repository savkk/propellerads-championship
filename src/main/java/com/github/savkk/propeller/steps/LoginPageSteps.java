package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.LoginPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPageSteps extends Steps<LoginPage> {
    private static final Logger log = LoggerFactory.getLogger(LoginPageSteps.class);

    @Step("Войти в систему под пользователем - {user}")
    public void login(String user, String password) {
        log.info("Войти в систему под пользователем - {}", user);
        $().field("Your Login").findElement(By.xpath("./parent::div")).click();
        $().field("Your Login").sendKeys(user);
        $().field("Your Password").findElement(By.xpath("./parent::div")).click();
        $().field("Your Password").sendKeys(password);
    }

    public LoginPageSteps(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    LoginPage $() {
        return open(LoginPage.class);
    }
}
