package com.github.savkk.propeller.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:aut.properties"})
public interface AUTConfig extends Config {

    @DefaultValue("http://localhost:8080")
    @Key("aut.baseurl")
    String baseUrl();
}
