package com.smartcity.its.manager.violation.repository;

import com.its.module.model.response.Response;
import com.smartcity.its.manager.violation.model.request.SmsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "email-service", url = "${EMAIL_SERVER_URL}")
public interface EmailFeignClient {

	@GetMapping("/orders/v1/orders/email")
	Response<String> sendEmail(@RequestParam(value = "subject") String subject,
			@RequestParam(value = "fromemail") String fromemail, @RequestParam(value = "toemail") String toemail,
			@RequestParam(value = "body") String body);

	@PostMapping("/orders/v1/orders/sms")
	Response<String> sendSms(@RequestBody SmsRequest smsRequest);
}
