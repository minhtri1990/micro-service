package com.smartcity.its.manager.violation.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViolationDto {
    private String id;
    private ViolationTypeDto violationType;
    private String deviceId;
    private String location;
    private String address;
    private Long timestamp;
    private String licensePlate;
    private String vehicleType;
    private String vehicleColor;
    private Boolean isViolation;
    private ViolationStatusDto statusDto;
    private String description;
}
