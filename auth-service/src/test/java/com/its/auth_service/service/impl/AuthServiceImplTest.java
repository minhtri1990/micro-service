//package com.its.auth_service.service.impl;
//
//import com.google.gson.Gson;
//import com.its.auth_service.model.entity.UserEntity;
//import com.its.auth_service.model.odp.response.ODPResponse;
//import com.its.auth_service.model.odp.response.ODPTokenData;
//import com.its.auth_service.model.odp.response.ODPUserData;
//import com.its.auth_service.model.request.LoginRequest;
//import com.its.auth_service.model.request.TokenRequest;
//import com.its.auth_service.repository.OdpFeignClient;
//import com.its.auth_service.repository.PermissionRepository;
//import com.its.auth_service.repository.UserRepository;
//import com.its.module.model.redis.RedisData;
//import com.its.module.model.redis.RedisKey;
//import com.its.module.model.response.Response;
//import org.hamcrest.Matchers;
//import org.junit.Before;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.platform.commons.util.ReflectionUtils;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class AuthServiceImplTest {
//
//	@Mock
//	private OdpFeignClient odpFeignClient;
//	@Mock
//	private UserRepository userRepository;
//	@Mock
//	private PermissionRepository permissionRepository;
//	@Mock
//	private RedisTemplate<RedisKey, RedisData> redisTemplate;
//
//	@InjectMocks
//	AuthServiceImpl service;
//
//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.initMocks(this);
//	}
//
//	ODPResponse<ODPUserData> odpUserResponseSuccess;
//	ODPResponse<ODPTokenData> odpTokenResponseSuccess;
//	ODPResponse odpSuccess;
//	ODPResponse odpFail;
//	ODPTokenData odpTokenData;
//	ODPUserData odpUserData;
//	UserEntity userEntity;
//	String email = "xxxxxx@gmail.com";
//	String username = "aaaaa";
//	String ssoId = "23232323";
//	String token = "fahfkjhasfiuashbdska";
//	Integer id = 7;
//	Integer role = 1;
//	Set<Integer> permissions;
//
//
//	@BeforeEach
//	public void setBean() {
//		System.out.println("before each");
//		odpUserData = new ODPUserData();
//		odpUserData.setId(ssoId);
//		odpUserData.setUsername(username);
//
//		odpTokenData = new ODPTokenData();
//		odpTokenData.setToken(token);
//		odpTokenData.setTokenType("bearer");
//		odpTokenData.setUserInfo(odpUserData);
//
//		odpUserResponseSuccess = new ODPResponse<>();
//		odpUserResponseSuccess.setResultCode(200);
//		odpUserResponseSuccess.setResultMessage("success");
//		odpUserResponseSuccess.setData(odpUserData);
//		when(odpFeignClient.getUserInfo(eq(token), any())).thenReturn(odpUserResponseSuccess);
//
//		odpTokenResponseSuccess = new ODPResponse<>();
//		odpTokenResponseSuccess.setResultCode(200);
//		odpTokenResponseSuccess.setResultMessage("success");
//		odpTokenResponseSuccess.setData(odpTokenData);
//		when(odpFeignClient.login(any(),any())).thenReturn(odpTokenResponseSuccess);
//
//		userEntity = UserEntity.builder()
//				.userName(username)
//				.email(email)
//				.isActive(true)
//				.ssoId(ssoId)
//				.lockedDate(0L)
//				.id(id)
//				.build();
//		when(userRepository.findBySsoId(eq(ssoId))).thenReturn(userEntity);
//		when(userRepository.findByIdAndIsActiveIsTrue(eq(id))).thenReturn(userEntity);
//		when(userRepository.findUserByEmail(eq(email))).thenReturn(userEntity);
//
//		permissions = new HashSet<>(Arrays.asList(1,2,20));
//		when(permissionRepository.findIdsByRole(eq(role))).thenReturn(permissions);
//
//		odpSuccess = new ODPResponse();
//		odpSuccess.setResultCode(200);
//		when(odpFeignClient.logout(any(), eq(token))).thenReturn(odpSuccess);
//
//		odpFail = new ODPResponse();
//		odpFail.setResultCode(400);
//		ReflectionTestUtils.setField(service, "LOCK_DURATION", 120000L);
//		ValueOperations<RedisKey, RedisData> valueOperations = mock(ValueOperations.class);
//		RedisData redisData = RedisData.builder().data(0).build();
//		when(valueOperations.get(any())).thenReturn(redisData);
//		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//	}
//
//	@Test
//	void login2() throws Exception {
//		LoginRequest loginRequest = new LoginRequest();
//		loginRequest.setUserName(username);
//		when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(userEntity);
//		Response actualResult = service.login(loginRequest);
//
//		assertThat(actualResult, Matchers.notNullValue());
//		assertThat(actualResult.getCode(), Matchers.equalTo(200));
//	}
//
//	@Test
//	void loginThrow() throws Exception {
//		LoginRequest loginRequest = new LoginRequest();
//		loginRequest.setUserName(username);
//		when(redisTemplate.hasKey(any())).thenReturn(true);
//		when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(userEntity);
//		odpTokenResponseSuccess.setResultCode(400);
//		odpTokenResponseSuccess.setResultMessage("Ok");
//		ReflectionTestUtils.setField(service, "LOCK_TIMES", 0);
//		Assertions.assertThrows(Exception.class, () -> service.login(loginRequest));
//	}
//
//	@Test
//	void loginWithEmail() throws Exception {
//		LoginRequest loginRequest = new LoginRequest();
//		loginRequest.setUserName(email);
//		when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(userEntity);
//		Response actualResult = service.login(loginRequest);
//
//		assertThat(actualResult, Matchers.notNullValue());
//		assertThat(actualResult.getCode(), Matchers.equalTo(200));
//	}
//
//	@Test
//	void loginWithEmailNotfound() throws Exception {
//		LoginRequest loginRequest = new LoginRequest();
//		loginRequest.setUserName(email);
//
//		when(userRepository.findUserByEmail(any())).thenReturn(null);
//		Assertions.assertThrows(Exception.class, () -> {
//			service.login(loginRequest);
//		});
//	}
//
//	@Test
//	void getUserByToken() throws Exception {
//		TokenRequest tokenRequest = new TokenRequest();
//		tokenRequest.setToken(token);
//		ReflectionTestUtils.setField(service, "DEFAULT_TOKEN", "dds");
//		Response actualResponse = service.getUserByToken(tokenRequest);
//		assertThat(actualResponse, Matchers.notNullValue());
//		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
//	}
//
//	@Test
//	void getUserByTokenThrow() throws Exception {
//		TokenRequest tokenRequest = new TokenRequest();
//		tokenRequest.setToken(token);
//
//		odpUserResponseSuccess.setResultCode(405);
//		Assertions.assertThrows(Exception.class, () ->service.getUserByToken(tokenRequest));
//	}
//
//	@Test
//	void signout() throws Exception {
//		Response actualResponse = service.signout(token);
//		assertThat(actualResponse, Matchers.notNullValue());
//		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
//	}
//
//	@Test
//	void signout0ThrowException2() throws Exception {
//		when(odpFeignClient.logout(any(), any())).thenReturn(odpFail);
//		Assertions.assertThrows(Exception.class, () -> {
//			service.signout(token);
//		});
//	}
//}