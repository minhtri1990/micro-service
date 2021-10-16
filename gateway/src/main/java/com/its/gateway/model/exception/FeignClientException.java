package com.its.gateway.model.exception;

import lombok.Data;

@Data
public class FeignClientException extends RuntimeException {
    public FeignClientException(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
    private Integer code;
    private String message;
}
