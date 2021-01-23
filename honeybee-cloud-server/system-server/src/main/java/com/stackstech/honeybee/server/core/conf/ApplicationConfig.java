package com.stackstech.honeybee.server.core.conf;

import com.stackstech.honeybee.server.core.enums.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "app.config")
public class ApplicationConfig {

    private String upload;
    private String api;

    public String getUpload() {
        if (StringUtils.isNotEmpty(upload) && !upload.endsWith(Constant.URL_SEPARATOR)) {
            upload = upload + Constant.URL_SEPARATOR;
        }
        return upload;
    }

}
