package com.github.savkk.propeller;

import com.github.savkk.propeller.config.AutConfig;
import com.github.savkk.propeller.config.WebDriverConfig;
import io.qameta.allure.Allure;
import io.qameta.atlas.core.Atlas;
import io.qameta.atlas.webdriver.WebDriverConfiguration;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;

public class PropellerTests {
    private Atlas atlas;
    private WebDriver webDriver;
    private GenericContainer aut;
    private WebDriverConfig webDriverConfig = ConfigFactory.create(WebDriverConfig.class);
    private AutConfig autConfig = ConfigFactory.create(AutConfig.class);

    @BeforeTest
    public void initAut() {
        aut = new FixedHostPortGenericContainer(autConfig.dockerImage())
                .withFixedExposedPort(autConfig.hostPort(), autConfig.containerPort());
        aut.start();
    }

    @BeforeMethod
    public void initDriver() {
        webDriver = WebDriverFactory.getInstance(webDriverConfig);
        atlas = new Atlas(new WebDriverConfiguration(webDriver, "http://localhost:" + autConfig.hostPort()));
    }

    @Test
    public void firstTest() {
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (!result.isSuccess() && webDriver != null) {
            Allure.addAttachment("failure-screenshot",
                    new ByteArrayInputStream(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES)));
        }
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @AfterTest
    public void stopAut() {
        aut.stop();
    }
}
