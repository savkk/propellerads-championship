package com.github.savkk.propeller.pages;

import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.WebPage;
import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;

public interface LoginPage extends WebPage {
    @FindBy("//input[@placeholder='{{ placeholder }}']")
    AtlasWebElement field(@Param("placeholder") String placeholderText);

    @FindBy("//button[normalize-space(text())='{{ buttonText }}']")
    AtlasWebElement button(@Param("buttonText") String buttonText);
}
