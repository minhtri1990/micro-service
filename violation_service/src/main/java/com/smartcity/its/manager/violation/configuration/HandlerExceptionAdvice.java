package com.smartcity.its.manager.violation.configuration;

import com.its.module.advice.HandleExceptionControllerAdvice;
import com.its.module.model.response.Response;
import com.smartcity.its.manager.violation.configuration.WebConfig;

import feign.codec.DecodeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerExceptionAdvice extends HandleExceptionControllerAdvice {
	private static final Logger LOGGER = LoggerFactory.getLogger(HandlerExceptionAdvice.class);
	
    @Override
    protected String translateMessage(String message) {
        return WebConfig.translate(message);
    }
}
