package com.its.auth_service.service.impl;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalDateTime;

import com.its.auth_service.configuration.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.its.auth_service.model.entity.CaptchaEntity;
import com.its.auth_service.repository.CaptchaRepository;
import com.its.auth_service.service.CaptchaService;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.redis.RedisData;
import com.its.module.model.redis.RedisKey;
import com.its.module.model.response.Response;
import com.its.module.utils.CaptchaUtils;
import com.its.module.utils.Constants;
import com.its.module.utils.Constants.RedisType;
import com.its.module.utils.StringUtils;

@Service
public class CaptchaServiceImpl implements CaptchaService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceImpl.class);

	@Autowired
	private CaptchaRepository captchaRepository;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public Response<?> getOtpToken() {
		String otpToken = genOTPToken();
		return Response.builder()
				.code(HttpStatus.OK.value())
				.message(WebConfig.translate("process_success"))
				.data(otpToken).build();
	}

	@Override
	public BufferedImage getImage(String validateCode) {
		if (validateCode == null)
			throw new BadRequestException(WebConfig.translate("invalidate.captcha"));
//		CaptchaEntity captchaEntity = captchaRepository.findByValidateCodeAndIsActiveIsTrue(validateCode);
//		if (captchaEntity == null)
//			captchaEntity = new CaptchaEntity();
//		captchaEntity.setValidateCode(validateCode);
//		captchaEntity.setCreatedDate(LocalDateTime.now());
//		captchaEntity.setCode(CaptchaUtils.getRandomCaptchaCode());
//		captchaEntity.setUsedTimes(0);
//		try {
//			captchaEntity = captchaRepository.save(captchaEntity);
//		} catch (Exception e) {
//			LOGGER.error("captcha trung", e);
//			throw new BadRequestException(WebConfig.translate("invalidate.code"));
//		}
		String code = CaptchaUtils.getRandomCaptchaCode();
		RedisKey redisKey = RedisKey.builder().id(validateCode).type(RedisType.CAPTCHA.ordinal()).build();
		RedisData redisData = RedisData.builder().data(code).createdDate(LocalDateTime.now()).build();
		redisTemplate.opsForValue().set(redisKey, redisData, Duration.ofMinutes(3));
		return CaptchaUtils.getCaptchaImage(code, 80, 30);
	}

	/**
	 * store otp token
	 * 
	 * @param
	 * @return
	 */
	public String genOTPToken() {
		String otpToken = StringUtils.getRandomString(20);
		RedisKey redisKey = RedisKey.builder().id(otpToken).type(Constants.RedisType.OTP_TOKEN.ordinal()).build();
		RedisData redisData = RedisData.builder().data(null).createdDate(LocalDateTime.now()).build();
		redisTemplate.opsForValue().set(redisKey, redisData, Duration.ofMinutes(3));
		return otpToken;
	}

	/**
	 * validate token
	 * 
	 * @param otpToken
	 * @return
	 */
	public boolean validateToken(String otpToken) {
		RedisKey redisKey = RedisKey.builder().type(RedisType.OTP_TOKEN.ordinal()).id(otpToken).build();
		if (redisTemplate.hasKey(redisKey)) {
			redisTemplate.delete(redisKey);
			return true;
		}
		return false;

	}
}
