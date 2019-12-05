package com.github.savkk.propeller.elements;

import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.extension.FindBy;

public interface Field extends AtlasWebElement {
    @FindBy("./parent::div")
    AtlasWebElement activator();
}
