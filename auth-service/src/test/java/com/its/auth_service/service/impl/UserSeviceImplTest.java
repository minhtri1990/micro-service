//package com.its.auth_service.service.impl;
//
//import com.its.auth_service.model.entity.OtpEntity;
//import com.its.auth_service.model.entity.UserEntity;
//import com.its.auth_service.model.odp.request.ODPLoginRequest;
//import com.its.auth_service.model.odp.response.ODPResponse;
//import com.its.auth_service.model.odp.response.ODPTokenData;
//import com.its.auth_service.model.odp.response.ODPUserData;
//import com.its.auth_service.model.request.*;
//import com.its.auth_service.repository.*;
//import com.its.auth_service.service.CaptchaService;
//import com.its.module.model.redis.RedisData;
//import com.its.module.model.response.Response;
//import org.junit.Before;
//import org.junit.jupiter.api.*;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import com.its.auth_service.model.entity.RoleEntity;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class UserSeviceImplTest {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private CaptchaRepository captchaRepository;
//    @Mock
//    private OtpRepository otpRepository;
//    @Mock
//    private OdpFeignClient odpFeignClient;
//    @Mock
//    private RoleRepository roleRepository;
//    @Mock
//    private EmailFeignClient emailFeignClient;
//    @Mock
//    private RedisTemplate redisTemplate;
//    @InjectMocks
//    UserSeviceImpl service;
//
//    @BeforeEach
//    void setUp() {
//        service = new UserSeviceImpl();
//        MockitoAnnotations.initMocks(this);
//    }
//
//    String username = "aaaa";
//    String email = "vvvvv@gmail.com";
//    String password = "1234678aA@";
//    String fullName = "Ngurye rejreroe";
//    String dob = "12/12/2012";
//    String ssoId = "dasdad";
//    Integer id = 1;
//    ODPUserData odpUserData;
//    ODPResponse<ODPUserData> odpUserResponse;
//    UserEntity userEntity;
//    List<UserEntity> userEntities;
//    ODPResponse<ODPTokenData> odpTokenResponseSuccess;
//    ODPTokenData odpTokenData;
//    String token = "fahfkjhasfiuashbdska";
//    Integer role = 1;
//    Set<Integer> permissions;
//
//    @BeforeEach
//    public void setBean() {
//        odpUserData = new ODPUserData();
//        odpUserData.setUsername(username);
//        odpUserData.setId(ssoId);
//
//        odpUserResponse = new ODPResponse<>();
//        odpUserResponse.setResultCode(200);
//        odpUserResponse.setData(odpUserData);
//        odpUserResponse.setResultMessage("Thanh cong");
//
//        userEntity = UserEntity.builder()
//                .userName(username)
//                .email(email)
//                .ssoId(ssoId)
//                .id(id)
//                .role(1)
//                .gender(1)
//                .isActive(true)
//                .build();
//        userEntities = new LinkedList<>(Arrays.asList(userEntity));
//        when(userRepository.findAll()).thenReturn(userEntities);
//        when(userRepository.findByIdAndIsActiveIsTrue(eq(id))).thenReturn(userEntity);
//        when(userRepository.searchUsers(any(), any(), any(), any(), any(), any())).thenReturn(userEntities);
//
//        odpUserData = new ODPUserData();
//        odpUserData.setId(ssoId);
//        odpUserData.setUsername(username);
//
//        odpTokenData = new ODPTokenData();
//        odpTokenData.setToken(token);
//        odpTokenData.setTokenType("bearer");
//        odpTokenData.setUserInfo(odpUserData);
//
//        when(odpFeignClient.getUserInfo(eq(token), any())).thenReturn(odpUserResponse);
//
//        odpTokenResponseSuccess = new ODPResponse<>();
//        odpTokenResponseSuccess.setResultCode(200);
//        odpTokenResponseSuccess.setResultMessage("success");
//        odpTokenResponseSuccess.setData(odpTokenData);
//        when(odpFeignClient.login(anyString(),any(ODPLoginRequest.class))).thenReturn(odpTokenResponseSuccess);
//
//        userEntity = UserEntity.builder()
//                .userName(username)
//                .email(email)
//                .isActive(true)
//                .ssoId(ssoId)
//                .id(id)
//                .role(1)
//                .build();
//        when(userRepository.findBySsoId(eq(ssoId))).thenReturn(userEntity);
//        when(userRepository.findByIdAndIsActiveIsTrue(eq(id))).thenReturn(userEntity);
//        when(userRepository.findUserByEmail(eq(email))).thenReturn(userEntity);
//
//        RoleEntity roleEntity = mock(RoleEntity.class);
//        when(roleEntity.getId()).thenReturn(1);
//        when(roleEntity.getName()).thenReturn("giam sat");
//        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
//        when(roleRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(roleEntity);
//        when(roleRepository.findByIsActiveIsTrue()).thenReturn(roleEntities);
//    }
//
//
//
//    @Test
//    void getAllUser() {
//        Response response = service.getAllUser(new HashMap<>(),0, 10);
//        assertThat(response.getCode(), Matchers.equalTo(200));
//    }
//
//    @Test
//    void getUserInfo() {
//        Response actualResult = service.getUserInfo(id);
//        assertThat(actualResult, Matchers.notNullValue());
//        assertThat(actualResult.getCode(), Matchers.equalTo(200));
//    }
//
//    @Test
//    void getUserInfoThrowException() {
//        Assertions.assertThrows(Exception.class, () -> service.getUserInfo(77));
//    }
//
//    @Test
//    void changePassword() {
//        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
//        changePasswordRequest.setOldPassword("123");
//        changePasswordRequest.setNewPassword("1234");
//        ODPResponse odpResponse = new ODPResponse();
//        odpResponse.setResultCode(200);
//        when(odpFeignClient.changePassword(any(), any())).thenReturn(odpResponse);
//        when(odpFeignClient.login(any(), any())).thenReturn(odpTokenResponseSuccess);
//        Response actualResult = service.changePassword(username, changePasswordRequest);
//        assertThat(actualResult, Matchers.notNullValue());
//        assertThat(actualResult.getCode(), Matchers.equalTo(200));
//    }
//
//    @Test
//    void changeUserRole() {
//    }
//
//    @Test
//    void uploadFile() {
//    }
//
//    @Test
//    void extractUser() {
//        ResponseEntity actualResponse = service.extractUser(new HashMap<>(), 0, 10);
//        assertThat(actualResponse.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
//    }
//
//    @Test
//    void extractUserPdf() {
//        Map<String, String> map = new HashMap(){{put("file_type", "pdf");}};
//        ResponseEntity actualResponse = service.extractUser(map, 0, 10);
//        assertThat(actualResponse.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
//    }
//
//    @Test
//    void createUser() {
//        UserWithoutPasswordCreateRequest request = new UserWithoutPasswordCreateRequest();
//        request.setUserName("dinh");
//        request.setEmail("dinh@gmail.com");
//        request.setRole(1);
//        when(userRepository.countByUsernameOrEmail(any(), any())).thenReturn(0);
//        when(odpFeignClient.createUser(any(), any())).thenReturn(odpUserResponse);
//        when(emailFeignClient.sendEmail(any(), any(), any(), any())).thenReturn(null);
//        when(userRepository.save(any())).thenReturn(UserEntity.builder().userName("dinh").build());
//        ReflectionTestUtils.setField(service, "IS_DEV_MODE", true);
//        ReflectionTestUtils.setField(service, "SERVER_MODE", "dev");
//        Response actualResponse = service.createUser(request, 7);
//        assertThat(actualResponse.getCode(), Matchers.equalTo(201));
//    }
//
//    @Mock
//    private CaptchaService captchaService;
//    @Test
//    void forgotPassword() {
//        ForgotPasswordRequest request = new ForgotPasswordRequest();
//        request.setEmail(email);
//        CaptchaRequest captchaRequest = new CaptchaRequest();
//        captchaRequest.setCaptchaCode("456");
//        captchaRequest.setValidateCode("789");
//        request.setCaptcha(captchaRequest);
//        when(captchaService.genOTPToken()).thenReturn("31232131");
//        UserEntity userEntity = UserEntity.builder().id(1).email(email).build();
//        when(redisTemplate.hasKey(any())).thenReturn(true);
//        RedisData otpData = RedisData.builder().data("456").build();
//        ValueOperations opsValue = Mockito.mock(ValueOperations.class);
//        when(opsValue.get(any())).thenReturn(otpData);
//        when(redisTemplate.opsForValue()).thenReturn(opsValue);
//        when(userRepository.findUserByEmail(eq(email))).thenReturn(userEntity);
//        Response actualResponse = service.forgotPassword(request);
//        assertThat(actualResponse.getCode(), Matchers.equalTo(200));
//    }
//
//    @Test
//    void resendForgotPassword() {
//        ResentForgotPasswordRequest request = new ResentForgotPasswordRequest();
//        request.setEmail(email);
//        request.setOtpToken("123");
//        when(userRepository.findUserByEmail(eq(email))).thenReturn(UserEntity.builder().id(1).email(email).build());
//        when(captchaService.validateToken(eq("123"))).thenReturn(true);
//        when(captchaService.genOTPToken()).thenReturn("111");
//        ValueOperations opsValue = Mockito.mock(ValueOperations.class);
//        when(redisTemplate.opsForValue()).thenReturn(opsValue);
//        Response actualResponse = service.resendForgotPassword(request);
//        assertThat(actualResponse.getCode(), Matchers.equalTo(200));
//    }
//
//    @Test
//    void resetPassword() {
//        ResetPasswordRequest request = new ResetPasswordRequest();
//        request.setEmail(email);
//        request.setPassword("123");
//        request.setCode("456");
//        when(userRepository.findUserByEmail(eq(email))).thenReturn(UserEntity.builder().id(1).email(email).build());
//        OtpEntity otpEntity = OtpEntity.builder().id(1).code("456").isActive(true).email(email).createdDate(LocalDateTime.now()).build();
//
//     //   when(otpRepository.getLastByEmail(any(), any())).thenReturn(otpEntity);
//        ODPResponse odpResponse = new ODPResponse<>();
//        odpResponse.setResultCode(200);
//        when(redisTemplate.hasKey(any())).thenReturn(true);
//        RedisData otpData = RedisData.builder().data("456").build();
//        ValueOperations opsValue = Mockito.mock(ValueOperations.class);
//        when(opsValue.get(any())).thenReturn(otpData);
//        when(redisTemplate.opsForValue()).thenReturn(opsValue);
//        when(odpFeignClient.resetUserPassword(any(), any())).thenReturn(odpResponse);
//        ReflectionTestUtils.setField(service, "OTP_EXP", 10000);
//        Response actualResponse = service.resetPassword(request);
//        assertThat(actualResponse.getCode(), Matchers.equalTo(200));
//    }
//
//    @Test
//    void updateUserInfo() {
//        UserInfoRequest request = new UserInfoRequest();
//        request.setEmail(email);
//        UserEntity userEntity = UserEntity.builder().id(1).email(email).role(1).build();
//        when(userRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(userEntity);
//        when(userRepository.save(any())).thenReturn(userEntity);
//        when(userRepository.findUserByEmail(eq(email))).thenReturn(null);
//        Response actualResponse = service.updateUserInfo(1, request, 1);
//        assertThat(actualResponse.getCode(), Matchers.equalTo(200));
//    }
//}