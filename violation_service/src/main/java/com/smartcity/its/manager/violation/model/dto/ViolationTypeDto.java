package com.smartcity.its.manager.violation.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ViolationTypeDto {
    private Integer id;
    private Integer type;
    private String code;
    private String name;
    private Boolean isUrgent;
    private String content;
}
