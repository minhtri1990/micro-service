package com.its.auth_service.service.impl;

import com.google.gson.Gson;
import com.its.auth_service.configuration.WebConfig;
import com.its.auth_service.repository.PermissionRepository;
import com.its.module.model.redis.RedisData;
import com.its.module.model.redis.RedisKey;
import com.its.module.utils.Constants;
import com.its.module.utils.GsonUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.its.auth_service.model.dto.TokenDto;
import com.its.auth_service.model.entity.UserEntity;
import com.its.auth_service.model.odp.request.ODPLoginRequest;
import com.its.auth_service.model.odp.response.ODPResponse;
import com.its.auth_service.model.odp.response.ODPTokenData;
import com.its.auth_service.model.odp.response.ODPUserData;
import com.its.auth_service.model.request.LoginRequest;
import com.its.auth_service.model.request.TokenRequest;
import com.its.auth_service.repository.OdpFeignClient;
import com.its.auth_service.repository.UserRepository;
import com.its.auth_service.service.AuthService;
import com.its.module.model.dto.UserDto;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.response.Response;
import com.its.module.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private OdpFeignClient odpFeignClient;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Value("${odp.api-key}")
	private String API_KEY;

	@Value("${DEFAULT_TOKEN}")
	private String DEFAULT_TOKEN;

	@Value("${DEFAULT_SSOID}")
	private String DEFAULT_SSOID;

	@Value("${LOCK_DURATION}")
	private Long LOCK_DURATION;

	@Value("${LOCK_TIMES}")
	private Integer LOCK_TIMES;

	@Autowired
	private RedisTemplate<RedisKey, RedisData> redisTemplate;

	@Override
	public Response<?> login(LoginRequest loginRequest) {
		String userName = loginRequest.getUserName();
		// cho phep user dang nhap bang email
		if (StringUtils.isValidEmail(userName)) {
			UserEntity entity = userRepository.findUserByEmail(userName.toLowerCase());
			if (entity == null)
				throw new BadRequestException(WebConfig.translate("user.login_error"));
			loginRequest.setUserName(entity.getUserName());
		}

		RedisKey redisKey = RedisKey.builder().id(userName).type(3).build();
		UserEntity userEntity = userRepository.findByUsernameOrEmail(userName, userName);
		if(userEntity == null || !userEntity.getIsActive())
			throw new BadRequestException("Thông tin đăng nhập không đúng");
		if (System.currentTimeMillis() - userEntity.getLockedDate() < LOCK_DURATION)
			throw new BadRequestException(WebConfig.translate("Tài khoản đã bị tạm khóa do nhập sai mật khẩu quá nhiều lần"));

		ODPLoginRequest odpLoginRequest = GsonUtils.mapObject(loginRequest, ODPLoginRequest.class);
		ODPResponse<ODPTokenData> odpResponse = odpFeignClient.login(API_KEY, odpLoginRequest);

		if (odpResponse.getResultCode() == HttpStatus.OK.value()) {
			if (!userEntity.getSsoId().equals(odpResponse.getData().getUserInfo().getId()))
				throw new BadRequestException(WebConfig.translate("user.login_error"));

			RedisData redisData = RedisData.builder().data(0).createdDate(LocalDateTime.now()).build();
			redisTemplate.opsForValue().set(redisKey, redisData);
			TokenDto tokenDto = GsonUtils.mapObject(odpResponse.getData(), TokenDto.class);
			return Response.builder().code(HttpStatus.OK.value()).data(tokenDto).build();
		}

		RedisData redisData = null;
		if (redisTemplate.hasKey(redisKey)) {
			redisData = redisTemplate.opsForValue().get(redisKey);
			Integer count = (Integer) redisData.getData();
			count++;
			redisData.setData(count);
			if (count > LOCK_TIMES) {
				redisData.setData(0);
				redisTemplate.opsForValue().set(redisKey, redisData);
				userEntity.setLockedDate(System.currentTimeMillis());
				userRepository.save(userEntity);
				throw new BadRequestException("Tài khoản của bạn đã bị khóa do nhập sai mật khẩu quá 5 lần");
			}
		} else {
			redisData = RedisData.builder().data(1).createdDate(LocalDateTime.now()).build();
		}
		redisTemplate.opsForValue().set(redisKey, redisData);
		throw new BadRequestException(odpResponse.getResultMessage());
	}

	@Override
	public Response<?> signout(String token) {
		ODPResponse<?> odpResponse = odpFeignClient.logout(API_KEY, token);
		if (odpResponse.getResultCode() == HttpStatus.OK.value()) {
			return Response.builder()
					.code(HttpStatus.OK.value())
					.message(WebConfig.translate("user.logout_success"))
					.build();
		}
		throw new BadRequestException(WebConfig.translate("user.token_timeout"));
	}

	@Override
	public Response<?> getUserByToken(TokenRequest tokenRequest) {
		ODPResponse<ODPUserData> odpResponse;
		if(DEFAULT_TOKEN.equals(tokenRequest.getToken())) {
			odpResponse = new ODPResponse<>();
			odpResponse.setResultCode(200);
			odpResponse.setData(new ODPUserData());
			odpResponse.getData().setId(DEFAULT_SSOID);
		//	throw new RuntimeException();
		} else odpResponse = odpFeignClient.getUserInfo(tokenRequest.replaceToken(), API_KEY);


		if (odpResponse.getResultCode() == HttpStatus.OK.value()) {
			String ssoId = odpResponse.getData().getId();
			UserEntity userEntity = userRepository.findBySsoId(ssoId);
			if (userEntity == null || !userEntity.getIsActive())
				throw new BadRequestException(WebConfig.translate("user.token_timeout"));
			if (System.currentTimeMillis() - userEntity.getLockedDate() < LOCK_DURATION)
				throw new BadRequestException(WebConfig.translate("Tài khoản đã bị tạm khóa"));

			UserDto userDto = GsonUtils.mapObject(userEntity, UserDto.class);
			Set<Integer> permissions = permissionRepository.findIdsByRole(userEntity.getRole());
			userDto.setPermissions(permissions.stream().map(Object::toString).collect(Collectors.toSet()));
			return Response.builder().code(HttpStatus.OK.value()).data(userDto).build();
		} else {
			throw new BadRequestException(WebConfig.translate("user.token_timeout"));
		}
	}
}
