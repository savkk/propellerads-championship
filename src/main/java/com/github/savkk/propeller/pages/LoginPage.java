package com.github.savkk.propeller.pages;

import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.extension.FindBy;

public interface LoginPage extends BasePage {
    @FindBy("//img[contains(@src, 'sign')]")
    AtlasWebElement signInButton();

    @FindBy("//h4[text()='Welcome to Propeller Championship!']")
    AtlasWebElement title();
}
