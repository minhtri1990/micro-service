package com.smartcity.its.manager.violation.model.dto;

import com.its.module.utils.StringUtils;
import lombok.Data;

@Data
public class ViolationExtractDto {
    public ViolationExtractDto(ViolationDto violationDto) {
        this.violationType = violationDto.getViolationType().getName();
        this.address = violationDto.getAddress();
        this.violationTimestamp = StringUtils.convertTimestamp(violationDto.getTimestamp());
        this.licensePlate = violationDto.getLicensePlate();
        this.vehicleType = violationDto.getVehicleType();
        this.vehicleColor = violationDto.getVehicleColor();
        this.isViolation = violationDto.getIsViolation()? "Đã xác định":"Chưa xác định";
    }
    private String violationType;
    private String address;
    private String violationTimestamp;
    private String licensePlate;
    private String vehicleType;
    private String vehicleColor;
    private String isViolation;
}
