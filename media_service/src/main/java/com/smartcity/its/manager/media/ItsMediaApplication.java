package com.smartcity.its.manager.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ItsMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsMediaApplication.class, args);
    }

}
