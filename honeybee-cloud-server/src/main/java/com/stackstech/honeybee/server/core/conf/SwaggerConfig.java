package com.stackstech.honeybee.server.core.conf;

import com.google.common.collect.Lists;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.StatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Value("${swagger.enable:false}")
    private boolean enable;

    @Bean
    public Docket createSwaggerDocket() {
        List<Response> responseMessageList = Lists.newArrayList();
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(StatusCode.SUCCESS.getHttpCode())).description(StatusCode.SUCCESS.getMessage()).build());
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(StatusCode.NOT_FOUND.getHttpCode())).description(StatusCode.NOT_FOUND.getMessage()).build());
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(StatusCode.UNAUTHORIZED.getHttpCode())).description(StatusCode.UNAUTHORIZED.getMessage()).build());
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(StatusCode.INTERNAL_ERROR.getHttpCode())).description(StatusCode.INTERNAL_ERROR.getMessage()).build());
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(StatusCode.FORBIDDEN.getHttpCode())).description(StatusCode.FORBIDDEN.getMessage()).build());
        responseMessageList.add(new ResponseBuilder().code(String.valueOf(StatusCode.BAD_REQUEST.getHttpCode())).description(StatusCode.BAD_REQUEST.getMessage()).build());

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
