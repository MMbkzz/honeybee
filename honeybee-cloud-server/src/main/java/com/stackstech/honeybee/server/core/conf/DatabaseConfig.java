package com.stackstech.honeybee.server.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Deprecated
@Data
@RefreshScope
@ConfigurationProperties(prefix = "app.datasource.db")
public class DatabaseConfig {

    private String jdbcUrl;
    private String username;
    private String password;

}
