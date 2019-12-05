package com.github.savkk.propeller.pages;

import com.github.savkk.propeller.elements.WithButton;
import com.github.savkk.propeller.elements.WithField;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.WebPage;
import io.qameta.atlas.webdriver.extension.FindBy;

public interface ProfilePage extends WithField, WithButton, WebPage {

    @FindBy("//select[@id='paymentSystemSelect']")
    AtlasWebElement paymentSystemSelect();
}
