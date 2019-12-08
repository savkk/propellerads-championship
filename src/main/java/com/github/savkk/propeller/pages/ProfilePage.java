package com.github.savkk.propeller.pages;

import com.github.savkk.propeller.layout.DayOfPaymentBlock;
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

    @FindBy("//div[./label[text()='Select day of payment']]")
    DayOfPaymentBlock dayOfPaymentBlock();
}
