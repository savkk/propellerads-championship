package com.github.savkk.propeller;

import com.github.savkk.propeller.config.AutConfig;
import com.github.savkk.propeller.config.CredentialsConfig;
import com.github.savkk.propeller.config.TimeOutsConfig;
import com.github.savkk.propeller.config.WebDriverConfig;
import io.qameta.allure.Allure;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.ByteArrayInputStream;

public class PropellerAdsTests {
    private WebDriver webDriver;
    private GenericContainer aut;
    private WebDriverConfig webDriverConfig = ConfigFactory.create(WebDriverConfig.class);
    private AutConfig autConfig = ConfigFactory.create(AutConfig.class);
    protected static final TimeOutsConfig timeOutsConfig = ConfigFactory.create(TimeOutsConfig.class);
    protected CredentialsConfig credentialsConfig = ConfigFactory.create(CredentialsConfig.class);
    protected static final int WEBDRIVER_WAIT_TIMEOUT = timeOutsConfig.webDriverWait();

    @BeforeTest
    public void initAut() {
        if (autConfig.enableTestContainers()) {
            aut = new FixedHostPortGenericContainer(autConfig.dockerImage())
                    .withFixedExposedPort(autConfig.port(), autConfig.containerPort());
            aut.start();
        }
    }

    @BeforeMethod
    public void initDriver() {
        webDriver = WebDriverFactory.getInstance(webDriverConfig);
        webDriver.get(autConfig.host() + ":" + autConfig.port());
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
        if (autConfig.enableTestContainers() && aut != null) {
            aut.stop();
        }
    }

    protected WebDriver getWebDriver() {
        return webDriver;
    }
}
