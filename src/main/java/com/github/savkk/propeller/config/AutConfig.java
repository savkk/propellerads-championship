package com.github.savkk.propeller.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:aut.properties"})
public interface AutConfig extends Config {

    @DefaultValue("http://localhost")
    @Key("aut.host")
    String host();

    @DefaultValue("8080")
    @Key("aut.port")
    int port();

    @Key("aut.testcontainers.enable")
    boolean enableTestContainers();

    @Key("aut.testcontainers.docker-image")
    String dockerImage();

    @Key("aut.testcontainers.container-port")
    int containerPort();

}
