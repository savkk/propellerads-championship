package com.github.savkk.propeller.pages;

import com.github.savkk.propeller.layout.Card;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.ElementsCollection;
import io.qameta.atlas.webdriver.extension.FindBy;
import io.qameta.atlas.webdriver.extension.Param;

public interface ArticlesPage extends BasePage {
    @FindBy("//img[@id='avatar']")
    AtlasWebElement avatar();

    @FindBy("//div[contains(@class, 'tree-main-node')][./button[.='{{ mainNode }}']]/div[contains(@class,'sub-tree')]//button")
    ElementsCollection<AtlasWebElement> subTreeButtons(@Param("mainNode") String mainNode);

    @FindBy("//div[@id='dataCard']")
    Card card();

}
