package com.github.savkk.propeller;

import com.github.savkk.propeller.config.WebDriverConfig;
import io.github.bonigarcia.wdm.Architecture;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;
import static io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.BrowserType.CHROME;
import static org.openqa.selenium.remote.BrowserType.FIREFOX;

public class WebDriverFactory {
    private static final Logger log = LoggerFactory.getLogger(WebDriverFactory.class);

    private WebDriverFactory() {
        throw new IllegalStateException();
    }

    public static WebDriver getInstance(WebDriverConfig webDriverConfig) {
        String driverVersion = webDriverConfig.driverVersion();
        Architecture architecture = webDriverConfig.driverArchitecture();
        log.info("WebDriver - {}", webDriverConfig.driverType());
        log.info("WebDriver version - {}", webDriverConfig.driverVersion());
        log.info("WebDriver architecture - {}", webDriverConfig.driverArchitecture());
        WebDriver webDriver;
        switch (webDriverConfig.driverType().toLowerCase()) {
            case CHROME:
                webDriver = getChromeDriver(driverVersion, architecture);
                break;
            case FIREFOX:
                webDriver = getFirefoxDriver(driverVersion, architecture);
                break;
            default:
                throw new IllegalStateException("Нет реализации для создания инстанса дайвера - " + webDriverConfig.driverType());
        }
        webDriver.manage()
                .timeouts()
                .implicitlyWait(webDriverConfig.implicitlyWait(), SECONDS)
                .pageLoadTimeout(webDriverConfig.pageLoadTimeout(), SECONDS)
                .setScriptTimeout(webDriverConfig.scriptTimeout(), SECONDS);
        if (webDriverConfig.startMaximize()) {
            webDriver.manage().window().maximize();
        }
        return webDriver;
    }

    private static WebDriver getFirefoxDriver(String driverVersion, Architecture architecture) {
        firefoxdriver()
                .architecture(architecture)
                .version(driverVersion)
                .setup();
        FirefoxOptions options = new FirefoxOptions();
        return new FirefoxDriver(options);
    }

    private static WebDriver getChromeDriver(String driverVersion, Architecture architecture) {
        chromedriver()
                .architecture(architecture)
                .version(driverVersion)
                .setup();
        ChromeOptions options = new ChromeOptions();
        return new ChromeDriver(options);
    }
}
