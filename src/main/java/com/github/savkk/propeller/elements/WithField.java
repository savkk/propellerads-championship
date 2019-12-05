package com.github.savkk.propeller.elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;

public interface WithField {
    @FindBy("//input[@placeholder='{{ placeholder }}']")
    Field field(@Param("placeholder") String placeholderText);
}
