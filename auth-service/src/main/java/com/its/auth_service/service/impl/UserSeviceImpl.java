package com.its.auth_service.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.its.auth_service.configuration.WebConfig;
import com.its.auth_service.model.dto.RoleDto;
import com.its.auth_service.model.dto.UserFullDto;
import com.its.auth_service.model.dto.UserSearchDto;
import com.its.auth_service.model.entity.RoleEntity;
import com.its.auth_service.model.request.*;
import com.its.auth_service.repository.*;
import com.its.auth_service.utils.ExtractUtils;
import com.its.module.model.redis.RedisData;
import com.its.module.model.redis.RedisKey;
import com.its.module.model.response.Meta;
import com.its.module.utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.its.auth_service.model.dto.TokenDto;
import com.its.auth_service.model.entity.OtpEntity;
import com.its.auth_service.model.entity.UserEntity;
import com.its.auth_service.model.odp.request.ODPChangePasswordRequest;
import com.its.auth_service.model.odp.request.ODPCreateUserRequest;
import com.its.auth_service.model.odp.request.ODPLoginRequest;
import com.its.auth_service.model.odp.request.ODPResetPasswordRequest;
import com.its.auth_service.model.odp.response.ODPResponse;
import com.its.auth_service.model.odp.response.ODPTokenData;
import com.its.auth_service.model.odp.response.ODPUserData;
import com.its.auth_service.model.odp.response.UploadFileResponse;
import com.its.auth_service.service.CaptchaService;
import com.its.auth_service.service.UserService;
import com.its.auth_service.utils.OtpType;
import com.its.module.model.dto.UserDto;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.exception.BusinessException;
import com.its.module.model.exception.NotfoundException;
import com.its.module.model.response.Response;
import com.its.module.utils.Constants.UserRole;

@Service
public class UserSeviceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserSeviceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CaptchaRepository captchaRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private OdpFeignClient odpFeignClient;

	@Autowired
	private RedisTemplate redisTemplate;

	@Value("${cleaner.captcha.exp}")
	private Integer CAPTCHA_EXP;

	@Value("${cleaner.otp.exp}")
	private Integer OTP_EXP;

	@Value("${odp.api-key}")
	private String API_KEY;

	@Value("${SERVER_MODE}")
	private String SERVER_MODE;

	@Value("${IS_DEV_MODE}")
	private Boolean IS_DEV_MODE;

	@Autowired
	private EmailFeignClient emailFeignClient;

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private UploadFileFeignClient uploadFileFeignClient;

	@Override
	public Response<?> getAllUser(Map<String, String> params, Integer page, Integer pageSize) {
		int offset = (page-1)*pageSize;
		String username = params.get("username");
		if(username != null) username = username.trim()
				.replace("%","\\%").replace("_","\\_");
		String email = params.get("email");
		if(email != null) email = email.trim()
				.replace("%", "\\%").replace("_", "\\_");
		Integer role = params.containsKey("role")?Integer.valueOf(params.get("role")):null;
		Boolean isActive = params.containsKey("isActive")?Boolean.valueOf(params.get("isActive")):null;
		List<UserEntity> users = userRepository.searchUsers(username, email, role, isActive, pageSize, offset);
		int totalEl = users.size() == 0? 0:users.remove(users.size()-1).getId();
		Meta meta = Meta.builder()
				.totalElements(totalEl)
				.page(page)
				.pageSize(pageSize)
				.totalPages(MathUtils.calcNumPages(totalEl, pageSize))
				.build();
		return Response.builder()
				.code(200)
				.meta(meta)
				.data(convertDto(users))
				.build();
	}

	private final String FILE_TYPE = "file_type";
	@Override
	public ResponseEntity<?> extractUser(Map<String, String> params, Integer page, Integer pageSize) {
		int offset = (page-1)*pageSize;
		String username = params.get("username");
		if(username != null) username = username.trim()
				.replace("%","\\%").replace("_","\\_");
		String email = params.get("email");
		if(email != null) email = email.trim()
				.replace("%", "\\%").replace("_", "\\_");
		Integer role = params.containsKey("role")?Integer.valueOf(params.get("role")):null;
		Boolean isActive = params.containsKey("isActive")?Boolean.valueOf(params.get("isActive")):null;
		System.out.println(params.get("isActive") + " " + isActive);
		List<UserEntity> users = userRepository.searchUsers(username, email, role, isActive, pageSize, offset);
		if(users.size() > 0) users.remove(users.size() - 1);
		if(!params.containsKey(FILE_TYPE)) params.put(FILE_TYPE, "excel");
		try {
			ByteArrayResource resource;
			String contentType;
			switch (params.get(FILE_TYPE)) {
				case "pdf":
					resource = new ByteArrayResource(ExtractUtils.accountPdf(convertDto(users)).toByteArray());
					contentType = "application/pdf";
					break;
				default:
					resource = new ByteArrayResource(ExtractUtils.accountExcel(convertDto(users)).toByteArray());
					contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			}
			return ResponseEntity.ok()
					.contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType(contentType))
					.body(resource);
		} catch (Exception e) {
			LOGGER.error("extract excel ", e);
			throw new BadRequestException("Không thể trích xuất file");
		}
	}

	@Override
	public Response<?> createUser(UserWithoutPasswordCreateRequest userCreateRequest, Integer authId) {
		String username = userCreateRequest.getUserName();
		String email = userCreateRequest.getEmail();

		if (roleRepository.findByIdAndIsActiveIsTrue(userCreateRequest.getRole()) == null)
			throw new BadRequestException(WebConfig.translate("request.validate"), 1025, "role không tồn tại");
		if (StringUtils.isValidEmail(username))
			throw new BadRequestException(WebConfig.translate("request.validate"), 1023, "username khong duoc trung dinh dang voi email");
		if (userRepository.countByUsernameOrEmail(username, email) > 0)
			throw new BadRequestException(WebConfig.translate("user.existed"), 1024, "username hoặc email đã tồn tại");

		// send create request to odp server
		String password = StringUtils.getRandomPassword();
		ODPCreateUserRequest odpCreateUserRequest = new ODPCreateUserRequest();
		odpCreateUserRequest.setUserName(username);
		odpCreateUserRequest.setPassword(password);
		odpCreateUserRequest.setEnabled(true);
		ODPResponse<ODPUserData> odpResponse = odpFeignClient.createUser(API_KEY, odpCreateUserRequest);
		if (odpResponse.getResultCode() != 200)
			throw new BusinessException(400, odpResponse.getResultMessage());
		ODPUserData odpUserData = odpResponse.getData();
		if (odpUserData == null)
			throw new BadRequestException(WebConfig.translate("user.create_error"));

		// create copied user entity
		UserEntity userEntity = GsonUtils.mapObject(userCreateRequest, UserEntity.class);
		userEntity.setSsoId(odpUserData.getId());
		ObjectUtils.setCreateHistory(userEntity, authId);

		userEntity = userRepository.save(userEntity);
		String subject = "Mật khẩu đăng nhập hệ thống ITS";
		String body = String.format("Mật khẩu tài khoản %s của bạn là %s", userEntity.getUserName(), password);
		sendEmail(subject, email, body);

		LOGGER.info(String.format("Gửi mật khẩu đến email %s mật khẩu: %s", userEntity.getEmail(), password));
		Response response = Response.builder().code(HttpStatus.CREATED.value()).message(WebConfig.translate("process_success"))
				.data(convertDto(userEntity)).build();
		if(IS_DEV_MODE)
			response.setDevMessage("password: " + password);
		return response;
	}

	@Override
	public Response<?> getUserInfo(Integer userId) {
		UserEntity user = userRepository.findByIdAndIsActiveIsTrue(userId);
		if (user == null)
			throw new NotfoundException(WebConfig.translate("user.not_found"));
		return Response.builder().code(HttpStatus.OK.value()).data(convertDto(user)).build();
	}

	@Override
	public Response<?> forgotPassword(ForgotPasswordRequest request) {
		CaptchaRequest captchaRequest = request.getCaptcha();
//		Integer captchaId = captchaRepository.checkCaptchaValidation(captchaRequest.getValidateCode(),
//				captchaRequest.getCaptchaCode(), CAPTCHA_EXP);
//		if (captchaId == null) {
//			throw new BadRequestException(WebConfig.translate("invalid.captcha"));
//		}
		RedisKey captchaKey = RedisKey.builder().id(captchaRequest.getValidateCode()).type(Constants.RedisType.CAPTCHA.ordinal()).build();
		if(!redisTemplate.hasKey(captchaKey))
			throw new BadRequestException(WebConfig.translate("invalid.captcha"));
		RedisData captchaData = (RedisData) redisTemplate.opsForValue().get(captchaKey);
		System.out.println("captcha data " + captchaData);
		if(!captchaRequest.getCaptchaCode().equals(captchaData.getData()))
			throw new BadRequestException(WebConfig.translate("invalid.captcha"));
		redisTemplate.delete(captchaKey);

		UserEntity userEntity = userRepository.findUserByEmail(request.getEmail());
		if (userEntity == null)
			throw new BadRequestException(WebConfig.translate("user.email_not_found"));

//		OtpEntity otpEntity = new OtpEntity();
//		otpEntity.setEmail(userEntity.getEmail());
//		otpEntity.setType(OtpType.RESET_PASSWORD);
//		otpEntity.setCode(StringUtils.getRandomNumberString(6));
//		otpEntity.setCreatedDate(LocalDateTime.now());
//		otpEntity.setIsActive(true);


//		otpRepository.save(otpEntity);
		String otp = StringUtils.getRandomNumberString(6);
		RedisKey otpKey = RedisKey.builder().id(userEntity.getEmail()).type(Constants.RedisType.OTP_RESET_PASSWORD.ordinal()).build();
		RedisData otpData = RedisData.builder().data(otp).build();
		redisTemplate.opsForValue().set(otpKey, otpData, Duration.ofMinutes(5));

		String subject = "Mã xác nhận";
		String body = "Mã xác thực của bạn là : " + otp;
		System.out.println(body);
		sendEmail(subject, userEntity.getEmail(), body);

		String otpToken = captchaService.genOTPToken();
		LOGGER.info(String.format("Gửi mã xác thực đến email %s", userEntity.getEmail()));

		return Response.builder().code(HttpStatus.OK.value()).message(WebConfig.translate("code.send_success"))
				.data(otpToken).build();
	}

	/**
	 * lay otp token tu redis server kiem tra xem da tung gui thanh cong email truoc
	 * do 10p chua, neu chua thi khong hop le xoa otp token ngay sau khi su dung
	 * 
	 * @param resentForgotPasswordRequest
	 * @return
	 */
	@Override
	public Response<?> resendForgotPassword(ResentForgotPasswordRequest resentForgotPasswordRequest) {
		if (resentForgotPasswordRequest.getOtpToken() == null)
			throw new BadRequestException(WebConfig.translate("invalidate.otp"));
		UserEntity userEntity = userRepository.findUserByEmail(resentForgotPasswordRequest.getEmail());
		if (userEntity == null)
			throw new BadRequestException(WebConfig.translate("invalidate.email"));

		String token = resentForgotPasswordRequest.getOtpToken();
		if (!captchaService.validateToken(token)) {
			throw new BadRequestException(WebConfig.translate("user.token_timeout"));
		}

		String code = captchaService.genOTPToken();
		// send email;
		String otp = StringUtils.getRandomNumberString(6);
		RedisKey otpKey = RedisKey.builder().id(userEntity.getEmail()).type(Constants.RedisType.OTP_RESET_PASSWORD.ordinal()).build();
		RedisData otpData = RedisData.builder().data(otp).build();
		redisTemplate.opsForValue().set(otpKey, otpData, Duration.ofMinutes(5));

		String subject = "Mã xác nhận";
		String body = "Mã xác thực của bạn là : " + otp;
		sendEmail(subject, userEntity.getEmail(), body);
		LOGGER.info(String.format("Gửi lại mã xác thực đến email %s", userEntity.getEmail()));

		return Response.builder().code(HttpStatus.OK.value()).message(WebConfig.translate("otp.send_success"))
				.data(code).build();
	}

	@Override
	public Response<?> resetPassword(ResetPasswordRequest request) {
		UserEntity userEntity = userRepository.findUserByEmail(request.getEmail());
		if (userEntity == null)
			throw new BadRequestException(WebConfig.translate("user.email_not_found"));

		// check otp code
		RedisKey otpKey = RedisKey.builder().id(request.getEmail()).type(Constants.RedisType.OTP_RESET_PASSWORD.ordinal()).build();

		if(!redisTemplate.hasKey(otpKey))
			throw new BadRequestException(WebConfig.translate("invalidate.otp"));
		RedisData otpData = (RedisData) redisTemplate.opsForValue().get(otpKey);
		if(!request.getCode().equals(otpData.getData()))
			throw new BadRequestException(WebConfig.translate("invalidate.otp"));
		redisTemplate.delete(otpKey);

		// send change password request to odp server
		ODPResetPasswordRequest odpResetPasswordRequest = new ODPResetPasswordRequest();
		odpResetPasswordRequest.setUserName(userEntity.getUserName());
		odpResetPasswordRequest.setNewPassword(request.getPassword());
		ODPResponse<?> odpResponse = odpFeignClient.resetUserPassword(API_KEY, odpResetPasswordRequest);

		if (odpResponse.getResultCode() == HttpStatus.OK.value()) {
			return Response.builder().code(HttpStatus.OK.value()).data(WebConfig.translate("pass.reset_success"))
					.build();
		}
		throw new BadRequestException(odpResponse.getResultMessage());
	}

	@Override
	public Response<?> updateUserInfo(Integer userId, UserInfoRequest request, Integer authId) {
		UserEntity updateUser = userRepository.findByIdAndIsActiveIsTrue(userId);
		if (updateUser == null)
			throw new NotfoundException(WebConfig.translate("user.not_found"));
		// validate email

		UserEntity user = userRepository.findUserByEmail(request.getEmail());
		if (user != null && !user.getId().equals(userId))
			throw new BadRequestException(WebConfig.translate("email.exist"),
					1025, "email đã tồn tại ở một tài khoản khác");

		updateUser.setEmail(request.getEmail());
		updateUser.setFullName(request.getFullName());
		updateUser.setGender(request.getGender());
		updateUser.setPhone(request.getPhone());
		updateUser.setDob(request.getDob());
		ObjectUtils.setModifyHistory(updateUser, userId);
		updateUser = userRepository.save(updateUser);

		return Response.builder()
				.code(HttpStatus.OK.value())
				.message(WebConfig.translate("update_success"))
				.data(convertDto(updateUser))
				.build();
	}

	@Override
	public Response<?> updateUserInfoAdminRole(Integer userId, UserInfoAdminRequest request, Integer authId) {
		UserEntity updateUser = userRepository.findByIdAndIsActiveIsTrue(userId);
		if (updateUser == null)
			throw new NotfoundException(WebConfig.translate("user.not_found"));
		// validate email
		if (roleRepository.findByIdAndIsActiveIsTrue(request.getRole()) == null)
			throw new BadRequestException(WebConfig.translate("request.validate"), 1025, "role không tồn tại");

		UserEntity user = userRepository.findUserByEmail(request.getEmail());
		if (user != null && !user.getId().equals(userId))
			throw new BadRequestException(WebConfig.translate("email.exist"),
					1025, "email đã tồn tại ở một tài khoản khác");

		updateUser.setEmail(request.getEmail());
		updateUser.setFullName(request.getFullName());
		updateUser.setGender(request.getGender());
		updateUser.setPhone(request.getPhone());
		updateUser.setDob(request.getDob());
		updateUser.setRole(request.getRole());
		ObjectUtils.setModifyHistory(updateUser, userId);
		updateUser = userRepository.save(updateUser);

		return Response.builder()
				.code(HttpStatus.OK.value())
				.message(WebConfig.translate("update_success"))
				.data(convertDto(updateUser))
				.build();
	}

	@Override
	public Response<?> changePassword(String userName, ChangePasswordRequest request) {
		if (request.getNewPassword().equals(request.getOldPassword()))
			throw new BadRequestException(WebConfig.translate("invalidate.pass.same"));

		ODPChangePasswordRequest odpChangePasswordRequest = GsonUtils.mapObject(request, ODPChangePasswordRequest.class);
		odpChangePasswordRequest.setUserName(userName);
		ODPResponse<?> odpResponse = odpFeignClient.changePassword(API_KEY, odpChangePasswordRequest);
		if (odpResponse.getResultCode() == HttpStatus.OK.value()) {
			ODPLoginRequest odpLoginRequest = new ODPLoginRequest();
			odpLoginRequest.setUserName(userName);
			odpLoginRequest.setPassword(request.getNewPassword());
			ODPResponse<ODPTokenData> odpResponseLogin = odpFeignClient.login(API_KEY, odpLoginRequest);
			TokenDto tokenDto = GsonUtils.mapObject(odpResponseLogin.getData(), TokenDto.class);
			return Response.builder()
					.code(HttpStatus.OK.value())
					.message(WebConfig.translate("pass.change_success"))
					.data(tokenDto)
					.build();
		}
		throw new BadRequestException(odpResponse.getResultMessage());
	}

	@Override
	public Response<?> resetPasswordAdmin(Integer userId, Integer authId) {
		UserEntity user = userRepository.findByIdAndIsActiveIsTrue(userId);
		if(user == null)
			throw new NotfoundException(WebConfig.translate("not_found"));
		String password = StringUtils.getRandomPassword();
		ODPResetPasswordRequest odpResetPasswordRequest = new ODPResetPasswordRequest();
		odpResetPasswordRequest.setUserName(user.getUserName());
		odpResetPasswordRequest.setNewPassword(password);
		ODPResponse<?> odpResponse = odpFeignClient.resetUserPassword(API_KEY, odpResetPasswordRequest);
		if (odpResponse.getResultCode() == HttpStatus.OK.value()) {
			String subject = "Mật khẩu đăng nhập hệ thống ITS";
			String body = String.format("Mật khẩu tài khoản %s của bạn là %s", user.getUserName(), password);
			sendEmail(subject, user.getEmail(), body);

			LOGGER.info(String.format("Gửi mật khẩu đến email %s mật khẩu: %s", user.getEmail(), password));
			Response response = Response.builder()
					.code(HttpStatus.CREATED.value())
					.message(WebConfig.translate("process_success"))
					.build();
			if(IS_DEV_MODE)
				response.setDevMessage("password: " + password);
			return response;
		}
		throw new BadRequestException(odpResponse.getResultMessage(), 1027, "có vấn đề xảy ra với odp server");
	}

	@Override
	public Response<?> changeUserRole(Integer userId, UserRoleRequest userRoleRequest, Integer authId) {
		UserEntity userEntity = userRepository.findByIdAndIsActiveIsTrue(userId);
		if (userEntity == null)
			throw new NotfoundException(WebConfig.translate("user.not_found"));
		if (userEntity.getRole().equals(userRoleRequest.getRole()))
			throw new BadRequestException(WebConfig.translate("role.change.invalidate"));
		if (roleRepository.findByIdAndIsActiveIsTrue(userRoleRequest.getRole()) == null)
			throw new BadRequestException(WebConfig.translate("request.validate"));

		userEntity.setRole(userRoleRequest.getRole());
		ObjectUtils.setModifyHistory(userEntity, authId);
		userEntity = userRepository.save(userEntity);

		return Response.builder().code(HttpStatus.OK.value()).message(WebConfig.translate("process_success"))
				.data(convertDto(userEntity)).build();
	}

	@Value("${file.supportTypes}")
	private String [] SUPPORT_TYPES;

	@Override
	public Response<?> uploadFile(MultipartFile file, Integer userId) {
		if(!Arrays.asList(SUPPORT_TYPES).contains(FilenameUtils.getExtension(file.getOriginalFilename())))
			throw new BadRequestException("File không đúng định dạng");

		UserEntity userEntity = userRepository.findByIdAndIsActiveIsTrue(userId);
		Response<UploadFileResponse> imageUpload = null;
		if (userEntity == null)
			throw new NotfoundException(WebConfig.translate("user.not_found"));
		try {
			imageUpload = uploadFileFeignClient.uploadFile(file);
			if (imageUpload.getCode() != 200) {
				return imageUpload;
			}
		} catch (Exception e) {
			LOGGER.error("khong the upload file", e);
			throw new BadRequestException(e.getMessage());
		}
		// update user
		userEntity.setAvatar(imageUpload.getData().getFiles().get(0).getUrl());
		ObjectUtils.setModifyHistory(userEntity, userId);
		userEntity = userRepository.save(userEntity);

		return Response.builder().code(HttpStatus.OK.value()).message(WebConfig.translate("process_success"))
				.data(GsonUtils.mapObject(userEntity, UserDto.class)).build();

	}

	@Override
	public Response<?> deleteUser(Integer userId, Integer authId) {
		UserEntity user = userRepository.findByIdAndIsActiveIsTrue(userId);
		if(user == null)
			throw new NotfoundException(WebConfig.translate("not_found"));
		user.setIsActive(false);
		ObjectUtils.setModifyHistory(user, authId);
		userRepository.save(user);
		return Response.builder()
				.code(HttpStatus.NO_CONTENT.value())
				.message(WebConfig.translate("process_success"))
				.build();
	}

	private List<UserFullDto> convertDto(List<UserEntity> users) {
		List<UserFullDto> userDtos = new ArrayList<>();
		Map<Integer, RoleDto> mapRoles = new HashMap<>();
		for (RoleEntity role : roleRepository.findByIsActiveIsTrue())
			mapRoles.put(role.getId(), GsonUtils.mapObject(role, RoleDto.class));
		for(UserEntity user:users) {
			UserFullDto userFullDto = GsonUtils.mapObject(user, UserFullDto.class);
			userFullDto.setRole(mapRoles.get(user.getRole()));
			userDtos.add(userFullDto);
		}
		return userDtos;
	}

	private UserFullDto convertDto(UserEntity user) {
		UserFullDto userFullDto = GsonUtils.mapObject(user, UserFullDto.class);
		RoleEntity role = roleRepository.findByIdAndIsActiveIsTrue(user.getRole());
		userFullDto.setRole(GsonUtils.mapObject(role, RoleDto.class));
		return userFullDto;
	}

	@Value("${FROM_EMAIL}")
	private String fromEmail;

	private void sendEmail(String subject, String email, String body) {
		new Thread(() -> {
			if("dev".equals(SERVER_MODE))
				emailFeignClient.sendEmail(subject, fromEmail, email, body);
		}).start();
	}
}
