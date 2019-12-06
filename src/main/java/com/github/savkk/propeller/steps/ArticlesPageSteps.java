package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.ArticlesPage;
import io.qameta.allure.Step;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.ElementsCollection;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ru.yandex.qatools.matchers.webdriver.DisplayedMatcher.displayed;

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

    @Step("Кликнуть по кнопке {buttonTitle}")
    public void clickButton(String buttonTitle) {
        log.info("Кликнуть по кнопке {}", buttonTitle);
        $().button(buttonTitle).waitUntil("Кнопка " + buttonTitle + " не отобразилась", displayed(), WEBDRIVER_WAIT_TIMEOUT)
                .click();
    }

    @Step("Получить список статей в разделе {sectionTitle}")
    public ElementsCollection<AtlasWebElement> getArticleList(String sectionTitle) {
        log.info("Получить список статей в разделе {}", sectionTitle);
        return $().subTreeButtons(sectionTitle);
    }

    @Override
    protected ArticlesPage $() {
        return open(ArticlesPage.class);
    }
}
