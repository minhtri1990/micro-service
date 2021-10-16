package com.its.module.model.exception;

import org.springframework.http.HttpStatus;

public class NotfoundException extends BusinessException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotfoundException(String message) {
		super(HttpStatus.NOT_FOUND.value(), message);
	}
}
