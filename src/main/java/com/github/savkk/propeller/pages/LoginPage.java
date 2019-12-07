package com.github.savkk.propeller.pages;

import com.github.savkk.propeller.elements.WithButton;
import com.github.savkk.propeller.elements.WithField;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.WebPage;
import io.qameta.atlas.webdriver.extension.FindBy;

public interface LoginPage extends WithButton, WithField, WebPage {
    @FindBy("//img[contains(@src, 'sign')]")
    AtlasWebElement signInButton();

    @FindBy("//h4[text()='Welcome to Propeller Championship!']")
    AtlasWebElement title();
}