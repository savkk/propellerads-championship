package com.github.savkk.propeller.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:aut.properties"})
public interface AutConfig extends Config {

    @DefaultValue("http://localhost")
    @Key("aut.host")
    int host();

    @DefaultValue("8080")
    @Key("aut.port")
    int port();

    @DefaultValue("true")
    @Key("aut.testcontainers.enable")
    boolean enableTestContainers();

    @DefaultValue("qapropeller/qa-battle:latest")
    @Key("aut.testcontainers.docker-image")
    String dockerImage();

    @DefaultValue("8080")
    @Key("aut.testcontainers.container-port")
    int containerPort();

}
