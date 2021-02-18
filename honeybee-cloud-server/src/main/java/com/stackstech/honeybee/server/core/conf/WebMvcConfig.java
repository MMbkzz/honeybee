package com.stackstech.honeybee.server.core.conf;

import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.handler.MessageHandler;
import com.stackstech.honeybee.server.core.inteceptor.AuthenticationInterceptor;
import com.stackstech.honeybee.server.core.inteceptor.RequestAccountArgumentResolver;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "cors")
public class WebMvcConfig extends WebMvcConfigurationSupport {

    private String[] urls;

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private RequestAccountArgumentResolver requestAccountArgumentResolver;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(Constant.API_ENDPOINT_ROOT + "/**")
                .excludePathPatterns(Constant.API_ENDPOINT_ROOT + "/security/login");
        super.addInterceptors(registry);
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(requestAccountArgumentResolver);
        super.addArgumentResolvers(argumentResolvers);
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
        registry.addMapping(Constant.API_ENDPOINT_ROOT + "/**")
                .allowedOrigins(urls)
                .allowCredentials(true)
                .allowedMethods(Constant.WILDCARD)
                .maxAge(Constant.ACCESS_CONTROL_MAX_AGE_TIME);
        super.addCorsMappings(registry);
    }

    @Override
    protected Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(MessageHandler.of().getMessageSource());
        return validator;
    }

}
