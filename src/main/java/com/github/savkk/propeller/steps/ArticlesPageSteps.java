package com.github.savkk.propeller.steps;

import com.github.savkk.propeller.pages.ArticlesPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.ElementsCollection;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ru.yandex.qatools.matchers.webdriver.DisplayedMatcher.displayed;

public final class ArticlesPageSteps extends BasePageSteps<ArticlesPage> {
    private static final Logger log = LoggerFactory.getLogger(ArticlesPageSteps.class);

    public ArticlesPageSteps(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    @Step("Страница Articles загружена")
    public boolean isLoaded() {
        try {
            return onPage().button("Advertisers").isDisplayed() &&
                    onPage().button("Publishers").isDisplayed() &&
                    onPage().button("Top level clients").isDisplayed();
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Step("Получить описание из открытой статьи")
    public String getArticleDescription() {
        log.info("Получить описание из открытой статьи");
        String text = onPage().card().discription().getAttribute("value");
        log.info("Текст описания: \n{}", text);
        Allure.addAttachment("Текст", text);
        return text;
    }

    @Step("Получить список статей в разделе {sectionTitle}")
    public ElementsCollection<AtlasWebElement> getArticleList(String sectionTitle) {
        log.info("Получить список статей в разделе {}", sectionTitle);
        return onPage().subTreeButtons(sectionTitle);
    }

    @Override
    protected ArticlesPage onPage() {
        return open(ArticlesPage.class);
    }

    @Step("Прокрутить описание статьи до конца")
    public void scrollDownArticleDescription() {
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;",
                onPage().card().discription().waitUntil(displayed()));
    }

    @Step("Передвинуть слайдер на {offset} пикселей")
    public void scrollSlider(int offset) {
        WebElement slider = onPage().card().slider().waitUntil(displayed(), WEBDRIVER_WAIT_TIMEOUT);
        new Actions(getWebDriver()).dragAndDropBy(slider, offset, 0).perform();
    }

    @Step("Получить высоту изображения героя")
    public int getHeroImageHeight() {
        return onPage().card().heroImage().getSize().getHeight();
    }

    @Step("Получить ширину изображения героя")
    public int getHeroImageWidth() {
        return onPage().card().heroImage().getSize().getWidth();
    }

    @Step("Зайти на страницу профиля пользователя")
    public void openProfile() {
        onPage().avatar().waitUntil(displayed(), WEBDRIVER_WAIT_TIMEOUT).click();
    }
}
