package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.ProfilePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
