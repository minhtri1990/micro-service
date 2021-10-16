package com.its.auth_service.controller;

import com.its.module.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.auth_service.model.request.LoginRequest;
import com.its.auth_service.model.request.TokenRequest;
import com.its.auth_service.service.AuthService;
import com.its.module.model.exception.ForbiddenException;
import com.its.module.model.exception.UnauthorizedException;
import com.its.module.model.response.Response;
import com.its.module.utils.Constants.AuthHeader;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class AuthController {
	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public Response<?> login(@RequestBody @Valid LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping("/signout")
	public Response<?> signout(@RequestHeader(AuthHeader.AUTHORIZATION) String token) {
		if (token == null)
			throw new UnauthorizedException();
		return authService.signout(token);
	}

	@PostMapping("/token")
	public Response<?> getUserByToken(@RequestBody @Valid TokenRequest tokenRequest,
			@RequestHeader(AuthHeader.IS_INTERNAL) Boolean isInternal) {
		if (!Boolean.TRUE.equals(isInternal))
			throw new ForbiddenException();
		return authService.getUserByToken(tokenRequest);
	}
}
