package com.smartcity.its.manager.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.smartcity.its.manager.device.repository.IocFeignClient;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EntityScan("com.*")
public class ItsDeviceServiceApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(ItsDeviceServiceApplication.class, args);
	}
	
	@Autowired
	private IocFeignClient iocFeign;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(iocFeign.getCameras("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjgxYjkxMDg2YTQwNjY2MzBhZWNkNTk2MGRhYjk4NzlkN2QxMDg0MjljOGJjMGRjZDJkNjk4NWEyMTgwZWUzNzY4ZDQwYzc0NjExNGYyYjYxIn0.eyJhdWQiOiI0IiwianRpIjoiODFiOTEwODZhNDA2NjYzMGFlY2Q1OTYwZGFiOTg3OWQ3ZDEwODQyOWM4YmMwZGNkMmQ2OTg1YTIxODBlZTM3NjhkNDBjNzQ2MTE0ZjJiNjEiLCJpYXQiOjE2MjIwODA4NTcsIm5iZiI6MTYyMjA4MDg1NywiZXhwIjoxNjIyMDk1MjU3LCJzdWIiOiIxNiIsInNjb3BlcyI6W119.S7B357pDH3Wa058chLTDBj-iWAOhR7IYMua2nntPHiRXEOGvRSd-UNmUwRFLs-zhRLuUhgHCropZ1rGNb8m1qmBmUM3jsAgVIBtigwXsLSxuMBBRLRNQYqA2eb46XxX-E7M3tpswqbBZpsWS0LlMiUCx6yMQvkk_kMywKiPUtc0IPLiNeUqWJrdTtuZSA0g-iMPYfV--QSz_4liLnF2B8tfqHbB07GU6DYjuPiA5wsl4N1lQxi7iynbvHLkX7yZ_4f2oLqHZWaFV6gML_Q_EbMnZdBgr_jiCgckbRYrWjY5BLc5kCc7hrCRmAsLY0OT3s8onoVp1PIln_FVWh71Xfz-gG3MS_A8JmRZNR7Y5-hrh6RCz-heqBA4TT1Q3rh_pqehAEu0TQMyiFGwZh2ucKw8baQUGUNxBrJ396Lqqbj_IOmq_Gicn92hZufEi6hQbO6JIXQrvxAm6c_uJZCU_AJDU6EKtsVf5983h6VrNsRXzt2HQgw0_RXnCuBvvl-I45JbdrYh5HpMvKInGKGnBlpZPqEZ5DsCRQBe8tOZMa7cRSZk4VGKYjMpJYaOx915dlm5jJ9hzwQvooFlsfT5fxVcrr7cGOvs8sgK51CULfdLvb9qVqNduPv9-jtBcMeb4ugwOndWIrg5AYXU1bDJgFnBY32xR2jCZ7io88UK0QbE", 1, 2000));
	}
}