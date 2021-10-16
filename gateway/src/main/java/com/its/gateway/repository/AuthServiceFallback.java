package com.its.gateway.repository;

import com.its.module.model.dto.UserDto;
import com.its.module.model.request.TokenRequest;
import com.its.module.model.response.Response;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceFallback implements AuthServiceFeignClient {
    @Override
    public Response<UserDto> getUserByToken(String isInternal, TokenRequest tokenRequest) {
        return Response.<UserDto>builder().code(400).build();
    }
}
