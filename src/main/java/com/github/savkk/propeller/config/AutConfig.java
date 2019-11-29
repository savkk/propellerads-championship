package com.github.savkk.propeller.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.FIRST)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:aut.properties"})
public interface AutConfig extends Config {

    @DefaultValue("qapropeller/qa-battle:latest")
    @Key("aut.docker-image")
    String dockerImage();

    @DefaultValue("8080")
    @Key("aut.host-port")
    int hostPort();

    @DefaultValue("8080")
    @Key("aut.container-port")
    int containerPort();

}
