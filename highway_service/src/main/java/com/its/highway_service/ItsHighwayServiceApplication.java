package com.its.highway_service;

import com.its.highway_service.repository.HighwayRepository;
import com.its.module.model.entity.HighwayEntity;
import com.its.module.utils.GsonUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.ModelMap;

@SpringBootApplication
@EnableEurekaClient
@EntityScan("com.*")
public class ItsHighwayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItsHighwayServiceApplication.class, args);
	}
}
