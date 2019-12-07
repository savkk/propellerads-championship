package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.ArticlesPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.ElementsCollection;
import org.openqa.selenium.JavascriptExecutor;
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
            return $().button("Advertisers").isDisplayed() &&
                    $().button("Publishers").isDisplayed() &&
                    $().button("Top level clients").isDisplayed();
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

    @Step("Получить описание из открытой статьи")
    public String getArticleDescription() {
        log.info("Получить описание из открытой статьи");
        String text = $().card().discription().getAttribute("value");
        log.info("Текст описания: \n{}", text);
        Allure.addAttachment("Текст", text);
        return text;
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

    @Step("Кнопка 'buttonTitle' активна")
    public boolean buttonIsEnabled(String buttonTitle) {
        boolean enabled = $().button(buttonTitle)
                .waitUntil("Кнопка " + buttonTitle + " не отобразилась", displayed(), WEBDRIVER_WAIT_TIMEOUT)
                .isEnabled();
        Allure.addAttachment("Активна", enabled ? "да" : "нет");
        return enabled;
    }

    @Step("Прокрутить описание статьи до конца")
    public void scrollDownArticleDescription() {
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;",
                $().card().discription().waitUntil(displayed()));
    }
}
