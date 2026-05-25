package com.bizmaster.service.construction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.bizmaster.service.template", "com.bizmaster.service.construction"})
public class ConstructionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConstructionServiceApplication.class, args);
    }
}
