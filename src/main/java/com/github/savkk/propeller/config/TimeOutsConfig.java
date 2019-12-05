package com.github.savkk.propeller.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:timeouts.properties")
public interface TimeOutsConfig extends Config {

    @Key("webdriverwait")
    int webDriverWait();
}
