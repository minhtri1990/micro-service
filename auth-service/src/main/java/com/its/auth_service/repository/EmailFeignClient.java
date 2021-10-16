package com.its.auth_service.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.its.module.model.response.Response;

@FeignClient(name = "email-service", url = "${email.server-url}", fallback = EmailFeignClientFallback.class)
public interface EmailFeignClient {

	@GetMapping("/orders/v1/orders/email")
	Response<String> sendEmail(@RequestParam(value = "subject") String subject,
			@RequestParam(value = "fromemail") String fromemail, @RequestParam(value = "toemail") String toemail,
			@RequestParam(value = "body") String body);

}
