package com.stackstech.honeybee.server.core.conf;

import com.google.common.collect.Lists;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.StatusCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        List<ResponseMessage> responseMessageList = Lists.newArrayList();
        responseMessageList.add(new ResponseMessageBuilder().code(StatusCode.SUCCESS.getHttpCode()).message(StatusCode.SUCCESS.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(StatusCode.NOT_FOUND.getHttpCode()).message(StatusCode.NOT_FOUND.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(StatusCode.UNAUTHORIZED.getHttpCode()).message(StatusCode.UNAUTHORIZED.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(StatusCode.INTERNAL_ERROR.getHttpCode()).message(StatusCode.INTERNAL_ERROR.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(StatusCode.FORBIDDEN.getHttpCode()).message(StatusCode.FORBIDDEN.getMessage()).build());
        responseMessageList.add(new ResponseMessageBuilder().code(StatusCode.BAD_REQUEST.getHttpCode()).message(StatusCode.BAD_REQUEST.getMessage()).build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.stackstech.honeybee.server"))
                .paths(PathSelectors.any()).build().securitySchemes(Lists.newArrayList(apiKey()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Honeybee server")
                .description("Honeybee server api")
                .version("1.0").build();
    }

    private ApiKey apiKey() {
        return new ApiKey(
                Constant.AUTHORIZATION, Constant.AUTHORIZATION, "header");
    }

}
