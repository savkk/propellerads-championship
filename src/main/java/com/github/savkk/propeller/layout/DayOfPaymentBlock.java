package com.github.savkk.propeller.layout;

import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.extension.FindBy;

public interface DayOfPaymentBlock extends AtlasWebElement<DayOfPaymentBlock> {

    @FindBy(".//h6")
    AtlasWebElement currentValue();

    @FindBy(".//input[@type='range']")
    AtlasWebElement inputDayRange();
}
