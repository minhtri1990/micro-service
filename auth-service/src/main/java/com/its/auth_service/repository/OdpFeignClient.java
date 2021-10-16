package com.its.auth_service.repository;

import com.its.auth_service.model.odp.request.*;
import com.its.auth_service.model.odp.response.ODPResponse;
import com.its.auth_service.model.odp.response.ODPTokenData;
import com.its.auth_service.model.odp.response.ODPUserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.its.module.utils.Constants.*;

import java.util.List;

@FeignClient(name = "auth-service", url = "${odp.server-url}")
public interface OdpFeignClient {
	public String TOKEN_JWT = "token_jwt";
	
    @GetMapping("/auth/userInfo")
    ODPResponse<ODPUserData> getUserInfo(@RequestHeader(AuthHeader.TOKEN_JWT) String token,
                                         @RequestHeader(AuthHeader.AUTH_APIKEY) String apikey);

    @PostMapping("/auth/login")
    ODPResponse<ODPTokenData> login(@RequestHeader(AuthHeader.AUTH_APIKEY) String apikey,
                                    @RequestBody ODPLoginRequest loginRequest);

    @PostMapping("/auth/logout")
    ODPResponse<?> logout(@RequestHeader(AuthHeader.AUTH_APIKEY) String apikey,
                          @RequestHeader(AuthHeader.AUTHORIZATION) String token);

    @PostMapping("/user/createUser")
    ODPResponse<ODPUserData> createUser(@RequestHeader(AuthHeader.AUTH_APIKEY) String apikey,
                                        @RequestBody ODPCreateUserRequest createUserRequest);

    @PostMapping("/user/changePassword")
    ODPResponse<?> changePassword(@RequestHeader(AuthHeader.AUTH_APIKEY) String apikey,
                               @RequestBody ODPChangePasswordRequest odpChangePasswordRequest);

    @PostMapping("/user/getUserByIds")
    ODPResponse<List<ODPUserData>> getUserByIds(@RequestHeader(AuthHeader.AUTH_APIKEY) String apikey,
                                                @RequestBody ODPIdsRequest odpIdsRequest);

    @PostMapping("/user/updateUserStatus")
    ODPResponse<?> updateUserStatus(@RequestHeader(AuthHeader.AUTH_APIKEY) String apikey,
                                 @RequestBody ODPUserStatusRequest userStatusRequest);

    @PostMapping("/user/resetPassword")
    ODPResponse<?> resetUserPassword(@RequestHeader(AuthHeader.AUTH_APIKEY) String apikey,
                                  @RequestBody ODPResetPasswordRequest resetPasswordRequest);

}
