package com.github.savkk.propeller.pages;

import com.github.savkk.propeller.elements.WithButton;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.WebPage;
import io.qameta.atlas.webdriver.extension.FindBy;

public interface ArticlesPage extends WithButton, WebPage {
    @FindBy("//img[@id='avatar']")
    AtlasWebElement avatar();
}
