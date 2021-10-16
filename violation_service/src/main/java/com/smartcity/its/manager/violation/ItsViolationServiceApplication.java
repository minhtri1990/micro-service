package com.smartcity.its.manager.violation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EntityScan("com.*")
public class ItsViolationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItsViolationServiceApplication.class, args);
    }
}