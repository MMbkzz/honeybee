package com.stackstech.dcp.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Component
@Data
@ConfigurationProperties(prefix = "app.cors")
public class WebMvcConfig implements WebMvcConfigurer {

    private String[] urls;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(urls)
                .allowCredentials(true)
                .allowedMethods("*")
                .maxAge(3600);
    }

}
