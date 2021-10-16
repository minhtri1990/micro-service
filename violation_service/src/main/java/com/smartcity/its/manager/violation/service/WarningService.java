package com.smartcity.its.manager.violation.service;

import com.its.module.model.response.BaseResponse;

public interface WarningService {
    BaseResponse stopReport(Integer userId);
}
