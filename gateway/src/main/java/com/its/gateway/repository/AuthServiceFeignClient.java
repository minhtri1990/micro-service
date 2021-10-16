package com.its.gateway.repository;

import com.its.gateway.configuration.FeignClientExceptionConfiguration;
import com.its.gateway.configuration.HandleExceptionAdvice;
import com.its.module.model.dto.UserDto;
import com.its.module.model.request.TokenRequest;
import com.its.module.model.response.Response;
import com.its.module.utils.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "its-auth-service")
@Primary
public interface AuthServiceFeignClient {
    @PostMapping("/auth/token")
    Response<UserDto> getUserByToken(@RequestHeader(Constants.AuthHeader.IS_INTERNAL) String isInternal,
                                     @RequestBody TokenRequest tokenRequest);
}
