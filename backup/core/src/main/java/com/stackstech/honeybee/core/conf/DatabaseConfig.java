package com.stackstech.honeybee.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "app.datasource.db")
@Component
public class DatabaseConfig {

    private String jdbcUrl;
    private String username;
    private String password;

}