package com.github.savkk.propeller.elements;

import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;

public interface WithButton {
    @FindBy(".//button[normalize-space(text())='{{ buttonText }}']")
    AtlasWebElement button(@Param("buttonText") String buttonText);
}
