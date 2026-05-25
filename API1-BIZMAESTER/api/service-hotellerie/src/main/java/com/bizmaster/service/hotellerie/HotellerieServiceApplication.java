package com.bizmaster.service.hotellerie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.bizmaster.service.template", "com.bizmaster.service.hotellerie"})
public class HotellerieServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotellerieServiceApplication.class, args);
    }
}
