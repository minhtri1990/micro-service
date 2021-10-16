package com.its.module.model.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		super(HttpStatus.BAD_REQUEST.value(), message);
	}

	public BadRequestException(String message, Integer errorCode) {
		super(HttpStatus.BAD_REQUEST.value(), message, errorCode);
	}

	public BadRequestException(String message, Integer errorCode, String devMessage) {
		super(HttpStatus.BAD_REQUEST.value(), message, errorCode, devMessage);
	}
}
