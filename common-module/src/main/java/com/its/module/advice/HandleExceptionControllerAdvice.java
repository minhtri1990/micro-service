package com.its.module.advice;

import com.google.gson.Gson;
import com.its.module.model.exception.*;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.its.module.model.response.BaseResponse;
import com.its.module.model.response.Response;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class HandleExceptionControllerAdvice {
    protected String translateMessage(String message) {
        return message;
    }

    protected boolean isDevMode() {
        return true;
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<?> errorException(BusinessException exception) {
        //	String message = translateMessage(exception.getMessage());
        Response<?> error = Response.builder()
                .code(exception.getStatus())
                .message(exception.getMessage())
                .build();
        if(isDevMode()) {
            error.setErrorCode(exception.getErrorCode());
            error.setDevMessage(exception.getDevMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.valueOf(exception.getStatus()));
    }

    @ExceptionHandler(value = FieldException.class)
    public ResponseEntity<?> nullFieldException(FieldException exception) {
        String message = translateMessage(exception.getField()) + " " + translateMessage(exception.getMessage());
        BaseResponse error = Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .errorCode(exception.getErrorCode())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FeignClientException.class)
    public ResponseEntity<?> feignException(FeignClientException exception) {
        BaseResponse error = Response.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> forbbidenException(AccessDeniedException exception) {
        BaseResponse error = Response.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(translateMessage("forbidden"))
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> runtimeError(RuntimeException e) {
        e.printStackTrace();
        BaseResponse error = Response.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Có lỗi xảy ra").errorCode(9999).devMessage(e.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response<?> constraintViolationException(ConstraintViolationException exception) {
        String ans = "";
        for(ConstraintViolation violation: exception.getConstraintViolations()) {
            ans += violation.getMessage();
        }
        return Response.builder()
                .code(400)
                .data(ans)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response<?> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String res = translateMessage(fieldError.getField()) + " " + translateMessage(fieldError.getDefaultMessage());
        return Response.builder().code(400).message(res).build();
    }
}
