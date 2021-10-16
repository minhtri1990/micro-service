//package com.its.auth_service.service.impl;
//
//import com.its.auth_service.model.entity.CaptchaEntity;
//import com.its.auth_service.repository.CaptchaRepository;
//import com.its.module.model.redis.RedisData;
//import com.its.module.model.redis.RedisKey;
//import com.its.module.utils.Constants;
//import com.its.module.utils.StringUtils;
//import org.hamcrest.Matchers;
//import org.junit.Ignore;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//import java.awt.image.BufferedImage;
//import java.time.LocalDateTime;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//class CaptchaServiceImplTest {
//	@Mock
//	private CaptchaRepository captchaRepository;
//	@Mock
//	private RedisTemplate redisTemplate;
//	@InjectMocks 
//	CaptchaServiceImpl service;
//
//	@BeforeEach
//	void setUp() {
//		service = new CaptchaServiceImpl();
//		MockitoAnnotations.initMocks(this);
//	}
//
//	CaptchaEntity captchaEntity;
//	String validateCode = "test";
//
//	@BeforeEach
//	void setBean() {
//		captchaEntity = CaptchaEntity.builder()
//				.validateCode(validateCode)
//				.code("321312")
//				.isActive(true)
//				.build();
//		when(captchaRepository.save(any(CaptchaEntity.class))).thenReturn(captchaEntity);
//	}
//
//	@Test
//	void getImage0() throws Exception {
//		String validateCode = new String("test");
//		when(captchaRepository.findByValidateCodeAndIsActiveIsTrue(any())).thenReturn(null);
//		ValueOperations opsValue = Mockito.mock(ValueOperations.class);
//		when(redisTemplate.opsForValue()).thenReturn(opsValue);
//		Object actualResult = service.getImage(validateCode);
//		assertThat(actualResult, Matchers.notNullValue());
//	}
//
//	@Test
//	void getImageThrowException0() throws Exception {
//		String validateCode = new String("test");
//		ValueOperations opsValue = Mockito.mock(ValueOperations.class);
//		when(redisTemplate.opsForValue()).thenReturn(opsValue);
//		Object actualResult = service.getImage(validateCode);
//		assertThat(actualResult, Matchers.notNullValue());
//	}
//
//}