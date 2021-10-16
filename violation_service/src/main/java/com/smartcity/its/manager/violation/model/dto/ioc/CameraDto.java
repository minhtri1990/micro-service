package com.smartcity.its.manager.violation.model.dto.ioc;

import java.util.List;

import lombok.Data;

@Data
public class CameraDto {
	private String id;
	private String type;
	private String deviceState;
	private List<String> ipAddress;
	private CameraConfigurationDto configuration;
}
