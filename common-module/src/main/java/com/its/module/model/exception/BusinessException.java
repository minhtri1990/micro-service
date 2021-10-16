package com.its.module.model.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String message;
    private Integer status;
    private Integer errorCode;
    private String devMessage;

    public BusinessException(Integer status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public BusinessException(Integer status, String message, Integer errorCode) {
        this(status, message);
        this.errorCode = errorCode;
    }

    public BusinessException(Integer status, String message, Integer errorCode, String devMessage) {
        this(status, message, errorCode);
        this.devMessage = devMessage;
    }
}
