package com.stackstech.honeybee.server.core.conf;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.config")
public class ApplicationConfig {

    private String upload;
    private String api;

    public String getUpload() {
        if (StringUtils.isNotEmpty(upload) && !upload.endsWith("/")) {
            upload = upload + "/";
        }
        return upload;
    }

}
