package com.github.savkk.propeller.pages;

import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;

public interface ProfilePage extends BasePage {

    @FindBy("//select[@id='paymentSystemSelect']")
    AtlasWebElement paymentSystemSelect();

    @FindBy("//a[text()='{{ menuTitle }}']")
    AtlasWebElement menuButton(@Param("menuTitle") String menuTitle);


    @FindBy("//div[@class='invalid-feedback'][normalize-space(.)='{{ message }}']")
    AtlasWebElement invalidFeedback(@Param("message") String expecteMessage);
}
