package com.smartcity.its.manager.violation.model.request;

import lombok.Data;

@Data
public class ViolationInfoRequest {
    private Integer status;
    private Boolean isViolation;
}
