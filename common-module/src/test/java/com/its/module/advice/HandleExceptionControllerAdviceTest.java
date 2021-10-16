package com.its.module.advice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import com.its.module.model.exception.BusinessException;
import com.its.module.model.exception.FeignClientException;
import com.its.module.model.response.Response;
class HandleExceptionControllerAdviceTest {
    HandleExceptionControllerAdvice advice;

    @BeforeEach
    void setUp() {
        advice = new HandleExceptionControllerAdvice();
    }

    @Test
    void errorException() {
        ResponseEntity responseEntity = advice.errorException(new BusinessException(400, "aaa"));
        MatcherAssert.assertThat(responseEntity, Matchers.notNullValue());
    }

    @Test
    void feignException() {
        ResponseEntity responseEntity = advice.feignException(new FeignClientException(400, "null"));
        MatcherAssert.assertThat(responseEntity, Matchers.notNullValue());
    }


    @Test
    void forbiddenException() {
        ResponseEntity responseEntity = advice.forbbidenException(new AccessDeniedException("null"));
        MatcherAssert.assertThat(responseEntity, Matchers.notNullValue());
    }
    
    @Test
    void constraintViolationException() {
    	ConstraintViolation<Object> constraintViolation = mock(ConstraintViolation.class);
    	when(constraintViolation.getMessage()).thenReturn("111");
    	Set<ConstraintViolation<Object>> constraintViolations = new HashSet<ConstraintViolation<Object>>() {{ add(constraintViolation); }};
    	ConstraintViolationException exception = new ConstraintViolationException(constraintViolations);
        Response<?> responseEntity = advice.constraintViolationException(exception);
        MatcherAssert.assertThat(responseEntity, Matchers.notNullValue());
    }

    @Test
    void runtimeErrorException() {
    	RuntimeException exception = Mockito.mock(RuntimeException.class);
    	ResponseEntity<?> responseEntity = advice.runtimeError(exception);
        MatcherAssert.assertThat(responseEntity, Matchers.notNullValue());
    }
}