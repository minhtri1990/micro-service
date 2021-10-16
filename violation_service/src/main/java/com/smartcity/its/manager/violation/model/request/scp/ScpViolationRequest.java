package com.smartcity.its.manager.violation.model.request.scp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScpViolationRequest {
    private Boolean isViolation;
    private String description;
}
