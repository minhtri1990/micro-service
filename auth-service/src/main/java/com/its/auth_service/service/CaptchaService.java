package com.its.auth_service.service;

import com.its.module.model.response.Response;

import java.awt.image.BufferedImage;

public interface CaptchaService {
	Response getOtpToken();

	BufferedImage getImage(String validateCode);

	String genOTPToken();

	boolean validateToken(String token);
}
