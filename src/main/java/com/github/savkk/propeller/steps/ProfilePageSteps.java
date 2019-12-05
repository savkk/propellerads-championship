package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.ProfilePage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProfilePageSteps extends PageSteps<ProfilePage> {
    private static final Logger log = LoggerFactory.getLogger(ProfilePageSteps.class);

    public ProfilePageSteps(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected ProfilePage $() {
        return open(ProfilePage.class);
    }

    @Override
    public boolean isLoaded() {
        return true;
    }
}
