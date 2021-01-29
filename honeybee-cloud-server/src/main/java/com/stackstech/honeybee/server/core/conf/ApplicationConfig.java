package com.stackstech.honeybee.server.core.conf;

import com.stackstech.honeybee.server.core.enums.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
@Configuration
public class ApplicationConfig {

    @Value("${app.config.upload:/tmp}")
    private String upload;

    @Value("${app.config.api:null}")
    private String api;

    @Value("${app.config.datacache.expires:300}")
    private Integer dataCacheExpires;

    public String getUpload() {
        if (StringUtils.isNotEmpty(upload) && !upload.endsWith(Constant.URL_SEPARATOR)) {
            upload = upload + Constant.URL_SEPARATOR;
        }
        return upload;
    }

}
