package com.github.savkk.propeller.config;

import io.github.bonigarcia.wdm.Architecture;
import org.aeonbits.owner.Config;

@Config.Sources("classpath:webdriver.properties")
public interface WebDriverConfig extends Config {

    @DefaultValue("chrome")
    @Key("driver.type")
    String driverType();

    @Key("driver.version")
    String driverVersion();

    @Key("driver.architecture")
    Architecture driverArchitecture();

    @Key("driver.startMaximize")
    boolean startMaximize();

    @DefaultValue("20")
    @Key("driver.pageLoadTimeout")
    int pageLoadTimeout();

    @DefaultValue("0")
    @Key("driver.implicitlyWait")
    int implicitlyWait();

    @DefaultValue("20")
    @Key("driver.scriptTimeout")
    int scriptTimeout();
}
