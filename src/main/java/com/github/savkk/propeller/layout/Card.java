package com.github.savkk.propeller.layout;

import com.github.savkk.propeller.elements.Slider;
import com.github.savkk.propeller.elements.WithButton;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.extension.FindBy;

public interface Card extends AtlasWebElement<Card>, WithButton {

    @FindBy(".//h5[contains(@class, 'card-title')]")
    AtlasWebElement cardTitle();

    @FindBy("(.//p[contains(@class, 'card-text')])[1]")
    AtlasWebElement cardText();

    @FindBy(".//textarea")
    AtlasWebElement discription();

    @FindBy(".//img[@id='heroImage']")
    AtlasWebElement heroImage();

    @FindBy(".//span[contains(@class, ui-slider)]")
    Slider slider();
}
