package com.its.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@RestController
@EnableFeignClients
public class ItsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItsGatewayApplication.class, args);
	}
	
	@GetMapping
	public String home() {
		return "gateway home 9711";
	}
}
