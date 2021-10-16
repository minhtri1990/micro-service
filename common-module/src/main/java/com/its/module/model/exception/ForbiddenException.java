package com.its.module.model.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ForbiddenException(String message) {
		super(HttpStatus.FORBIDDEN.value(), message);
	}

	public ForbiddenException() {
		this("Bạn không có quyền thực hiện chức năng này.");
	}
}
