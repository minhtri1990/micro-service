package com.its.gateway.configuration;

import com.its.module.advice.HandleExceptionControllerAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class HandleExceptionAdvice extends HandleExceptionControllerAdvice {
    @Override
    protected String translateMessage(String message) {
        return WebConfig.translate(message);
    }
}
