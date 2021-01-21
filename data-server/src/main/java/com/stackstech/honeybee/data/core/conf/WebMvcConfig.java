package com.stackstech.honeybee.data.core.conf;

import com.stackstech.honeybee.data.core.enums.ApiEndpoint;
import com.stackstech.honeybee.data.core.inteceptor.AuthenticationInterceptor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Data
@Component
@ConfigurationProperties(prefix = "app.cors")
public class WebMvcConfig extends WebMvcConfigurationSupport {

    private String[] urls;

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(ApiEndpoint.API_ENDPOINT_ROOT + "/**")
                .excludePathPatterns(ApiEndpoint.API_ENDPOINT_ROOT + "/status");

        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
        super.addResourceHandlers(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(ApiEndpoint.API_ENDPOINT_ROOT + "/**")
                .allowedOrigins(urls)
                .allowCredentials(true)
                .allowedMethods("*")
                .maxAge(3600);
        super.addCorsMappings(registry);
    }

}
