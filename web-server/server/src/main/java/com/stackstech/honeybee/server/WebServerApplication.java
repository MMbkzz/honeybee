package com.stackstech.honeybee.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author william
 */
@SpringBootApplication
@ImportResource({"classpath*:shiro.xml"})
@ComponentScan({"com.stackstech.honeybee"})
@ServletComponentScan
@EnableDiscoveryClient
public class WebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }

}
