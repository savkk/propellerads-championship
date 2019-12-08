package com.github.savkk.propeller;

import com.github.savkk.propeller.steps.ArticlesPageSteps;
import com.github.savkk.propeller.steps.LoginPageSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.atlas.webdriver.ElementsCollection;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

@Feature("Страница информации о существующих клиентах")
public class ArticlesPageTests extends Fixtures {
    private static final String FILE_NAME = "data.txt";

    @BeforeMethod(description = "Вход в систему")
    public void login() {
        LoginPageSteps loginPageSteps = new LoginPageSteps(getWebDriver());
        Assert.assertTrue("Страница логина не загрузилась", loginPageSteps.isLoaded());
        loginPageSteps.signInByCookie(credentialsConfig.cookieSecretKey(), credentialsConfig.cookieSecretValue());
    }

    @Test
    @Story("Проверка количества статей по разделам")
    public void checkArticlesCount() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue("Страница со статьями не загрузилась", articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        SoftAssertions softAssertions = new SoftAssertions();
        ElementsCollection advertisers = articlesPageSteps.getArticleList("Advertisers");
        softAssertions.assertThat(advertisers).withFailMessage("Количество статей в разделе Advertisers отличается от ожидаемого")
                .hasSize(2);

        articlesPageSteps.clickButton("Publishers");
        ElementsCollection publishers = articlesPageSteps.getArticleList("Publishers");
        softAssertions.assertThat(publishers).withFailMessage("Количество статей в разделе Publishers отличается от ожидаемого")
                .hasSize(2);

        articlesPageSteps.clickButton("Top level clients");
        ElementsCollection topLevelClients = articlesPageSteps.getArticleList("Top level clients");
        softAssertions.assertThat(topLevelClients).withFailMessage("Количество статей в разделе Top level clients отличается от ожидаемого")
                .hasSize(10);
        softAssertions.assertAll();
    }

    @Test
    @Story("Проверка скачивания описания из статьи")
    public void checkArticleDownload() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue("Страница со статьями не загрузилась", articlesPageSteps.isLoaded());
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
        Assert.assertTrue("Страница со статьями не загрузилась", articlesPageSteps.isLoaded());
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
        Assert.assertTrue("Страница со статьями не загрузилась", articlesPageSteps.isLoaded());
        Cookie notSavedOpened = articlesPageSteps.getCookie("notSavedOpened");
        Assert.assertNull(notSavedOpened);
        articlesPageSteps.clickButton("Advertisers");
        notSavedOpened = articlesPageSteps.getCookie("notSavedOpened");
        Assert.assertEquals("Advertisers", notSavedOpened.getValue());
        articlesPageSteps.clickButton("Publishers");
        notSavedOpened = articlesPageSteps.getCookie("notSavedOpened");
        Assert.assertEquals(notSavedOpened.getValue(), "Advertisers,Publishers");
        articlesPageSteps.clickButton("Top level clients");
        notSavedOpened = articlesPageSteps.getCookie("notSavedOpened");
        Assert.assertEquals(notSavedOpened.getValue(), "Advertisers,Publishers,Top level clients");
    }

    @Test
    @Story("Проверка сохранения статьи")
    public void articleMoveToSavedTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue("Страница со статьями не загрузилась", articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        Cookie saved = articlesPageSteps.getCookie("saved");
        Assert.assertEquals("Test Advertiser", saved.getValue());
    }

    @Test
    @Story("Проверка сохранения статей из каждого раздела")
    public void articlesFromAllSectionsMoveToSavedTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue("Страница со статьями не загрузилась", articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        Cookie saved = articlesPageSteps.getCookie("saved");
        Assert.assertEquals("Значение куки отличается от ожидаемого", "Test Advertiser", saved.getValue());
        articlesPageSteps.clickButton("Publishers");
        articlesPageSteps.clickButton("Youtube");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        saved = articlesPageSteps.getCookie("saved");
        Assert.assertEquals("Значение куки отличается от ожидаемого", "Test Advertiser,Youtube", saved.getValue());
        articlesPageSteps.clickButton("Top level clients");
        articlesPageSteps.clickButton("Sasha Grey");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        saved = articlesPageSteps.getCookie("saved");
        Assert.assertEquals("Значение куки отличается от ожидаемого", "Test Advertiser,Youtube,Sasha Grey", saved.getValue());
    }

    @Test
    @Story("Проверка удаления статьи")
    public void articleRemoveFromSavedTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue("Страница со статьями не загрузилась", articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        articlesPageSteps.scrollDownArticleDescription();
        articlesPageSteps.clickButton("Move to saved");
        Cookie saved = articlesPageSteps.getCookie("saved");
        Assert.assertEquals("Значение куки отличается от ожидаемого", "Test Advertiser", saved.getValue());
        articlesPageSteps.clickButton("Removed from saved");
        saved = articlesPageSteps.getCookie("saved");
        Assert.assertNull("Кука должна быть пустой", saved);
    }

    @Test
    @Story("Проверить, что размеры изобращения героя изменяютя при помощи слайдера")
    public void changeHeroImageSizeTest() {
        ArticlesPageSteps articlesPageSteps = new ArticlesPageSteps(getWebDriver());
        Assert.assertTrue("Страница со статьями не загрузилась", articlesPageSteps.isLoaded());
        articlesPageSteps.clickButton("Advertisers");
        articlesPageSteps.clickButton("Test Advertiser");
        Dimension heroImageSize = articlesPageSteps.getHeroImageSize();
        articlesPageSteps.scrollSlider(500);
        Dimension newHeroImageSize = articlesPageSteps.getHeroImageSize();
        Assert.assertTrue("Размер изображения героя не изменился", newHeroImageSize.getHeight() > heroImageSize.getHeight()
                && newHeroImageSize.getWidth() > heroImageSize.getWidth());
        articlesPageSteps.scrollSlider(-500);
        newHeroImageSize = articlesPageSteps.getHeroImageSize();
        Assert.assertEquals("Размер изображения героя не вернулся к начальному значению", newHeroImageSize, heroImageSize);
    }
}
