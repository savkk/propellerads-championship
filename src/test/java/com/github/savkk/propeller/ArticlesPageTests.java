package com.github.savkk.propeller;

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
}
