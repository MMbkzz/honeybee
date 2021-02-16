package com.stackstech.honeybee.server.core.conf;

import com.stackstech.honeybee.server.core.enums.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Application config
 *
 * @author william
 * @since 1.0
 */
@Data
@Component
@RefreshScope
@Configuration
public class ApplicationConfig {

    @Value("${server.path.upload:/tmp}")
    private String uploadPath;

    @Value("${server.path.conf}")
    private String configPath;

    @Value("${server.api:null}")
    private String api;

    @Value("${server.datacache.expires:300}")
    private Integer dataCacheExpires;

    public String getUploadPath() {
        if (StringUtils.isNotEmpty(uploadPath) && !uploadPath.endsWith(Constant.URL_SEPARATOR)) {
            uploadPath = uploadPath + Constant.URL_SEPARATOR;
        }
        return uploadPath;
    }

}
