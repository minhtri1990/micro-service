package com.its.module.model.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BusinessException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String message) {
		super(HttpStatus.UNAUTHORIZED.value(), message);
	}

	public UnauthorizedException() {
		this("Bạn cần thực hiện đăng nhập lại.");
	}
}
