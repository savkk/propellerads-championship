package com.github.savkk.propeller.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:credentials.properties"})
public interface CredentialsConfig extends Config {

    @Key("aut.login")
    String login();

    @Key("aut.password")
    String password();

    @Key("aut.cookie.secret.key")
    String cookieSecretKey();

    @Key("aut.cookie.secret.value")
    String cookieSecretValue();
}
