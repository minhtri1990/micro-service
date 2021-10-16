package com.its.module.model.exception;

import lombok.Getter;

@Getter
public class FieldException extends RuntimeException {
    private String field;
    private String message;
    private Integer errorCode;

    public FieldException(String field, String message) {
        super(field);
        this.field = field;
        this.message = message;
    }

    public FieldException(String field, String message, Integer errorCode) {
        this(field, message);
        this.errorCode = errorCode;
    }
}
