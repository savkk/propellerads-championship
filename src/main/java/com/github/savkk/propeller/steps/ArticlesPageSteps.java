package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.ArticlesPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArticlesPageSteps extends PageSteps<ArticlesPage> {
    private static final Logger log = LoggerFactory.getLogger(ArticlesPageSteps.class);

    public ArticlesPageSteps(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public boolean isLoaded() {
        try {
            $().button("Advertisers").isDisplayed();
            $().button("Publishers").isDisplayed();
            $().button("Top level clients").isDisplayed();
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Override
    protected ArticlesPage $() {
        return open(ArticlesPage.class);
    }
}
