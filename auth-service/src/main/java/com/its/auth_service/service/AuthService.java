package com.its.auth_service.service;

import com.its.auth_service.model.odp.response.ODPResponse;
import com.its.auth_service.model.odp.response.ODPTokenData;
import com.its.auth_service.model.request.LoginRequest;
import com.its.auth_service.model.request.TokenRequest;
import com.its.module.model.response.Response;

public interface AuthService {
    Response login(LoginRequest loginRequest);
    Response signout(String token);
    Response getUserByToken(TokenRequest tokenRequest);
}
