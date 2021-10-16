package com.its.auth_service.configuration;

import com.its.module.advice.HandleExceptionControllerAdvice;
import com.its.module.model.exception.BusinessException;
import com.its.module.model.response.BaseResponse;
import com.its.module.model.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@ControllerAdvice
public class HandlerExceptionAdvice extends HandleExceptionControllerAdvice {
    @Override
    protected String translateMessage(String message) {
        return WebConfig.translate(message);
    }

    @Value("${IS_DEV_MODE}")
    private Boolean IS_DEV_MODE;

    @Override
    protected boolean isDevMode() {
        return IS_DEV_MODE;
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseEntity<?> fileSizeLimit(Exception e) throws IOException {
        BaseResponse error = Response.builder()
                .code(400)
                .message("File quá lớn")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<?> errorException(BusinessException exception) {
        Response<?> error = Response.builder().code(exception.getStatus()).message(exception.getMessage()).build();
        if (this.isDevMode()) {
            error.setErrorCode(exception.getErrorCode());
            error.setDevMessage(exception.getDevMessage());
        }

        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
