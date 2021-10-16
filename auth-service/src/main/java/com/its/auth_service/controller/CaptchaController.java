package com.its.auth_service.controller;

import com.its.auth_service.repository.EmailFeignClient;
import com.its.auth_service.service.CaptchaService;
import com.its.module.model.exception.ForbiddenException;
import com.its.module.model.response.BaseResponse;
import com.its.module.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
	@Autowired
	private CaptchaService captchaService;

	@RequestMapping(method = RequestMethod.GET, produces = "image/jpg")
	public @ResponseBody byte[] getCaptchaImage(@RequestParam("validateCode") String validateCode) throws IOException {
		BufferedImage bufferedImage = captchaService.getImage(validateCode);
		if (bufferedImage == null)
			return null;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", bao);
		return bao.toByteArray();
	}

	@GetMapping("/otp-token")
	public Object getOtpToken() {
		return captchaService.getOtpToken();
	}

	@GetMapping("/validate-token")
	public Object validateToken(@RequestParam("token") String token) {
		boolean result = captchaService.validateToken(token);
		return Response.builder().code(HttpStatus.OK.value()).message("validate token " + result).build();
	}
}
