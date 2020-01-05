package com.github.savkk.propeller.fixtures;

import com.github.savkk.propeller.WebDriverFactory;
import com.github.savkk.propeller.config.AutConfig;
import com.github.savkk.propeller.config.CredentialsConfig;
import com.github.savkk.propeller.config.TimeOutsConfig;
import com.github.savkk.propeller.config.WebDriverConfig;
import io.qameta.allure.Allure;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static org.openqa.selenium.remote.BrowserType.CHROME;
import static org.openqa.selenium.remote.BrowserType.FIREFOX;

public class Fixtures {
    private static final Logger log = LoggerFactory.getLogger(Fixtures.class);
    private WebDriver webDriver;
    private GenericContainer aut;
    private WebDriverConfig webDriverConfig = ConfigFactory.create(WebDriverConfig.class);
    private AutConfig autConfig = ConfigFactory.create(AutConfig.class);
    private Path tempDirectory;
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
    public void initDriver() throws IOException {
        tempDirectory = Files.createTempDirectory("propeller");
        File file = tempDirectory.toFile();
        file.deleteOnExit();
        Capabilities capabilities = getCapabilities();
        webDriver = WebDriverFactory.getInstance(webDriverConfig, capabilities);
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

    protected Path getTempDirectory() {
        return tempDirectory;
    }

    private Capabilities getCapabilities() {
        Capabilities capabilities;
        switch (webDriverConfig.driverType().toLowerCase()) {
            case CHROME:
                capabilities = new ChromeOptions();
                HashMap<String, Object> pref = new HashMap<>();
                pref.put("download.default_directory", tempDirectory.toAbsolutePath().toString());
                pref.put("download.prompt_for_download", false);
                pref.put("plugins.always_open_pdf_externally", true);
                ((ChromeOptions) capabilities).setExperimentalOption("prefs", pref);
                break;
            case FIREFOX:
                capabilities = new FirefoxOptions();
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("browser.download.folderList", 2);
                profile.setPreference("browser.download.dir", "C:\\Windows\\temp");
                profile.setPreference("browser.download.useDownloadDir", true);
                profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                        "application/pdf, application/octet-stream, application/x-winzip, application/x-pdf, application/x-gzip");
                ((FirefoxOptions) capabilities).setProfile(profile);
                break;
            default:
                throw new IllegalStateException("Нет реализации для создания инстанса дайвера - " + webDriverConfig.driverType());
        }
        return capabilities;
    }
}
