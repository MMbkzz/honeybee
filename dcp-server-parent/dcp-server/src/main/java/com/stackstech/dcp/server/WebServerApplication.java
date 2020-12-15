package com.stackstech.dcp.server;

import com.stackstech.dcp.connector.core.ResourceSessionManager;
import com.stackstech.dcp.core.conf.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource({"classpath*:shiro.xml"})
@ComponentScan({"com.stackstech.dcp"})
@ServletComponentScan
@EnableDiscoveryClient
public class WebServerApplication {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean("resourceSessionManager")
    public ResourceSessionManager get(ApplicationConfig applicationConfig) {
        return new ResourceSessionManager(applicationConfig.getUpload());
    }

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }
}
