package com.stackstech.honeybee.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "app.datasource.redis")
@Component
public class RedisCacheConfig {

    private String host;
    private String port;
    private String database;
    private String password;

}
