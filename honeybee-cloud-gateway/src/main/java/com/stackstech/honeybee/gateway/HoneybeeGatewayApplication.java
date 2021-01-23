package com.stackstech.honeybee.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HoneybeeGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoneybeeGatewayApplication.class, args);
    }
}
