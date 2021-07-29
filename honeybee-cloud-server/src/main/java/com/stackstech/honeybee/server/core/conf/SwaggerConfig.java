package com.stackstech.honeybee.server.core.conf;

import com.google.common.collect.Lists;
import com.stackstech.honeybee.server.core.enums.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableOpenApi
//加入注解
//@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable:false}")
    private boolean enable;

    @Bean
    public Docket createSwaggerDocket() {
        List<Response> responseMessageList = Lists.newArrayList();
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(HttpStatus.OK.value())).description(HttpStatus.OK.getReasonPhrase()).build());
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())).description(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(HttpStatus.FORBIDDEN.value())).description(HttpStatus.FORBIDDEN.getReasonPhrase()).build());

        return new Docket(DocumentationType.OAS_30)
                .globalResponses(HttpMethod.GET, responseMessageList)
                .globalResponses(HttpMethod.POST, responseMessageList)
                .globalResponses(HttpMethod.PUT, responseMessageList)
                .globalResponses(HttpMethod.DELETE, responseMessageList)
                .groupName(Constant.SERVER_NAME)
                .enable(enable)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.stackstech.honeybee.server"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("JWT").build()))
                .securityContexts(Collections.singletonList(SecurityContext.builder()
                        .securityReferences(Collections.singletonList(SecurityReference.builder()
                                .scopes(new AuthorizationScope[0])
                                .reference("JWT")
                                .build()))
                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                        .build()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Honeybee server")
                .description("Honeybee server api")
                .version("1.0").build();
    }

}
