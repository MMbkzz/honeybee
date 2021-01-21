package com.stackstech.honeybee.data.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.datasource.db")
public class DatabaseConfig {

    private String jdbcUrl;
    private String username;
    private String password;

}
