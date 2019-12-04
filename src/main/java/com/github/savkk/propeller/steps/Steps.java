package com.github.savkk.propeller.steps;

import io.qameta.atlas.core.Atlas;
import io.qameta.atlas.webdriver.WebDriverConfiguration;
import org.openqa.selenium.WebDriver;

abstract class Steps<T> {
    private final WebDriver webDriver;
    private final Atlas atlas;

    Steps(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.atlas = new Atlas(new WebDriverConfiguration(webDriver));
    }

    T open(Class<T> page) {
        return atlas.create(webDriver, page);
    }

    abstract T $();
}
