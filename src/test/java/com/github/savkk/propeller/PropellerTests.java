package com.github.savkk.propeller;

import com.github.savkk.propeller.config.AUTConfig;
import com.github.savkk.propeller.config.WebDriverConfig;
import io.qameta.atlas.core.Atlas;
import io.qameta.atlas.webdriver.WebDriverConfiguration;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PropellerTests {
    private Atlas atlas;
    private WebDriver webDriver;
    private WebDriverConfig webDriverConfig = ConfigFactory.create(WebDriverConfig.class);
    private AUTConfig autConfig = ConfigFactory.create(AUTConfig.class);

    @BeforeTest
    public void initDriver() {
        webDriver = WebDriverFactory.getInstance(webDriverConfig);
        atlas = new Atlas(new WebDriverConfiguration(webDriver, autConfig.baseUrl()));
    }

    @Test
    public void firstTest() {

    }

    @AfterTest
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
