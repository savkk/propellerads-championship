package com.github.savkk.propeller.tests;

import com.github.savkk.propeller.fixtures.Fixtures;
import com.github.savkk.propeller.steps.ArticlesPageSteps;
import com.github.savkk.propeller.steps.LoginPageSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.atlas.webdriver.ElementsCollection;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.github.savkk.propeller.constants.ErrorMessages.PAGE_IS_NOT_LOADED;

@Feature("Страница информации о существующих клиентах")
public class ArticlesPageTests extends Fixtures {
    private static final String FILE_NAME = "data.txt";

    @BeforeMethod(description = "Вход в систему")
    public void login() {
        LoginPageSteps loginPageSteps = new LoginPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, loginPageSteps.isLoaded());
        loginPageSteps.signInByCookie(credentialsConfig.cookieSecretKey(), credentialsConfig.cookieSecretValue());
    }

    @Test
    @Story("Проверка количества статей по разделам")
    public void checkArticlesCount() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        SoftAssertions softAssertions = new SoftAssertions();
        ElementsCollection advertisers = articlesPageSteps.getArticleList("Advertisers");
        softAssertions.assertThat(advertisers)
                .withFailMessage("Количество статей в разделе Advertisers отличается от ожидаемого")
                .hasSize(2);

        articlesPageSteps.clickButton("Publishers");
        ElementsCollection publishers = articlesPageSteps.getArticleList("Publishers");
        softAssertions.assertThat(publishers)
                .withFailMessage("Количество статей в разделе Publishers отличается от ожидаемого")
                .hasSize(2);

        articlesPageSteps.clickButton("Top level clients");
        ElementsCollection topLevelClients = articlesPageSteps.getArticleList("Top level clients");
        softAssertions.assertThat(topLevelClients)
                .withFailMessage("Количество статей в разделе Top level clients отличается от ожидаемого")
                .hasSize(10);
        softAssertions.assertAll();
    }

    @Test
    @Story("Проверка скачивания описания из статьи")
    public void checkArticleDownload() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        articlesPageSteps.clickButton("Download info");
        String articleDescription = articlesPageSteps.getArticleDescription();
        String textFromFile = articlesPageSteps.getTextFromFile(Paths.get(getTempDirectory().toString(), FILE_NAME));
        Assert.assertEquals(articleDescription, textFromFile);
    }

    @Test
    @Story("Проверка, что кнопка Move to saved становится доступной только после полного прочтения описания")
    public void moveToSavedTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        Assert.assertFalse("Кнопка Move to saved не должна быть активной",
                articlesPageSteps.buttonIsEnabled("Move to saved"));
        articlesPageSteps.scrollDownArticleDescription();
        Assert.assertTrue("Кнопка Move to saved должна быть активной",
                articlesPageSteps.buttonIsEnabled("Move to saved"));
    }

    @Test
    @Story("Проверка добавления открытых разделов в куки notSavedOpened")
    public void notSavedOpenedCookieTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, articlesPageSteps.isLoaded());
        String notSavedOpened = articlesPageSteps.getCookieValue("notSavedOpened");
        Assert.assertNull(notSavedOpened);
        articlesPageSteps.clickButton("Advertisers");
        notSavedOpened = articlesPageSteps.getCookieValue("notSavedOpened");
        Assert.assertEquals("Advertisers", notSavedOpened);
        articlesPageSteps.clickButton("Publishers");
        notSavedOpened = articlesPageSteps.getCookieValue("notSavedOpened");
        Assert.assertEquals(notSavedOpened, "Advertisers,Publishers");
        articlesPageSteps.clickButton("Top level clients");
        notSavedOpened = articlesPageSteps.getCookieValue("notSavedOpened");
        Assert.assertEquals(notSavedOpened, "Advertisers,Publishers,Top level clients");
    }

    @Test
    @Story("Проверка сохранения статьи")
    public void articleMoveToSavedTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        String saved = articlesPageSteps.getCookieValue("saved");
        Assert.assertEquals("Test Advertiser", saved);
    }

    @Test
    @Story("Проверка сохранения статей из каждого раздела")
    public void articlesFromAllSectionsMoveToSavedTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        String saved = articlesPageSteps.getCookieValue("saved");
        Assert.assertEquals("Значение куки отличается от ожидаемого", "Test Advertiser", saved);
        articlesPageSteps.clickButton("Publishers");
        articlesPageSteps.clickButton("Youtube");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        saved = articlesPageSteps.getCookieValue("saved");
        Assert.assertEquals("Значение куки отличается от ожидаемого", "Test Advertiser,Youtube", saved);
        articlesPageSteps.clickButton("Top level clients");
        articlesPageSteps.clickButton("Sasha Grey");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        saved = articlesPageSteps.getCookieValue("saved");
        Assert.assertEquals("Значение куки отличается от ожидаемого",
                "Test Advertiser,Youtube,Sasha Grey",
                saved);
    }

    @Test
    @Story("Проверка удаления статьи")
    public void articleRemoveFromSavedTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        String saved = articlesPageSteps.getCookieValue("saved");
        Assert.assertEquals("Значение куки отличается от ожидаемого", "Test Advertiser", saved);
        articlesPageSteps.clickButton("Removed from saved");
        saved = articlesPageSteps.getCookieValue("saved");
        Assert.assertNull("Кука должна быть пустой", saved);
    }

    @Test
    @Story("Проверить, что размеры изобращения героя изменяютя при помощи слайдера")
    public void changeHeroImageSizeTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue(PAGE_IS_NOT_LOADED, articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        int heroImageHeight = articlesPageSteps.getHeroImageHeight();
        int heroImageWidth = articlesPageSteps.getHeroImageWidth();
        articlesPageSteps.scrollSlider(500);
        int newHeroImageHeight = articlesPageSteps.getHeroImageHeight();
        int newHeroImageWidth = articlesPageSteps.getHeroImageWidth();
        Assert.assertTrue("Размер изображения героя не изменился", newHeroImageHeight > heroImageHeight
                && newHeroImageWidth > heroImageWidth);
        articlesPageSteps.scrollSlider(-500);
        newHeroImageHeight = articlesPageSteps.getHeroImageHeight();
        newHeroImageWidth = articlesPageSteps.getHeroImageWidth();
        Assert.assertEquals("Ширина изображения героя не вернулась к начальному значению",
                newHeroImageWidth, heroImageWidth);
        Assert.assertEquals("Высота изображения героя не вернулась к начальному значению",
                newHeroImageHeight, heroImageHeight);
    }
}
