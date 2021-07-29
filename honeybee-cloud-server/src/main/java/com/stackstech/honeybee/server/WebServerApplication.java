package com.stackstech.honeybee.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author william
 */
@SpringBootApplication
//引入Swagger的配置类
//@Import({com.stackstech.honeybee.server.core.conf.SwaggerConfig.class})
//@EnableSwagger2
//找不到xml配置文件
@ImportResource({"classpath*:shiro.xml"})
@ComponentScan({"com.stackstech.honeybee"})
@ServletComponentScan
@EnableDiscoveryClient
public class
WebServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }

}
