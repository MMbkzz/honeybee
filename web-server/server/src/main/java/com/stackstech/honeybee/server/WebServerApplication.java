package com.stackstech.honeybee.server;

import com.stackstech.honeybee.server.core.conf.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource({"classpath*:shiro.xml"})
@ComponentScan({"com.stackstech.honeybee"})
@ServletComponentScan
@EnableDiscoveryClient
public class WebServerApplication {

    @Autowired
    private ApplicationConfig applicationConfig;

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }
}
