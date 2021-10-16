package com.smartcity.its.manager.violation.service;

import com.its.module.model.response.BaseResponse;
import com.smartcity.its.manager.violation.model.request.ConfirmViolationRequest;
import com.smartcity.its.manager.violation.model.request.scp.ScpReceiveRequest;
import com.smartcity.its.manager.violation.model.request.scp.ScpViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface ViolationService {
    BaseResponse getOne(String id, Authentication authentication);
    BaseResponse search(Map<String, String> params, Integer page, Integer pageSize, Authentication authentication);
    ResponseEntity<?> extract(Map<String, String> params, Integer page, Integer pageSize, Authentication authentication);
    BaseResponse confirmNotViolated(String violationId, ConfirmViolationRequest confirmViolationRequest, Authentication authentication);
    BaseResponse receiveViolationFromScp(List<ScpViolation> entities);
    BaseResponse sendDaily();
}
