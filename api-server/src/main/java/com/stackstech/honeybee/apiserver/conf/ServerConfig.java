package com.stackstech.honeybee.apiserver.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "server")
@Component
@RefreshScope
public class ServerConfig {

    private String id;
    private String host;
    private Integer port;

    public String getReal() {
        return host + ":" + port;
    }

}
