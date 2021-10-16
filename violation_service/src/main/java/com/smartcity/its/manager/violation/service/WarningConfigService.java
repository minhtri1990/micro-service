package com.smartcity.its.manager.violation.service;

import org.springframework.security.core.Authentication;

import com.its.module.model.response.BaseResponse;
import com.smartcity.its.manager.violation.model.request.warning.WarningConfigRequest;

public interface WarningConfigService {
    BaseResponse getByUserIdAndType(Integer userId, Integer type);
    BaseResponse update(Integer userId, WarningConfigRequest warningConfigRequest, Authentication authentication);
}
