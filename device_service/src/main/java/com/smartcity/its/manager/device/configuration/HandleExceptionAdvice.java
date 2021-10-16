package com.smartcity.its.manager.device.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.its.module.advice.HandleExceptionControllerAdvice;

@ControllerAdvice
public class HandleExceptionAdvice extends HandleExceptionControllerAdvice {
    @Override
    protected String translateMessage(String message) {
        return WebConfig.translate(message);
    }

    @Value("${IS_DEV_MODE}")
    private boolean IS_DEV_MODE;

    @Override
    protected boolean isDevMode() {
        return IS_DEV_MODE;
    }
}
