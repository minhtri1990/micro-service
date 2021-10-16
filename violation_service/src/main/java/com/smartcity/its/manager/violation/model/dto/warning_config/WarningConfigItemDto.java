package com.smartcity.its.manager.violation.model.dto.warning_config;

import java.util.List;

import com.smartcity.its.manager.violation.model.dto.ViolationTypeDto;

import lombok.Data;

@Data
public class WarningConfigItemDto {
    private Integer violationTypeId;
    private ViolationTypeDto violationTypeDto;
    private Integer sendingType;
    private List<Integer> sendingMethod;
    private Integer repetition;
}
