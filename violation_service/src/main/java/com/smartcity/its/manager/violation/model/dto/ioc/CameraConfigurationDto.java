package com.smartcity.its.manager.violation.model.dto.ioc;

import java.util.List;

import lombok.Data;

@Data
public class CameraConfigurationDto {
	private String tenant;
	private String deviceId;
	private String siteId;
	private Float latitude;
	private Float longitude;
	private List<CameraStreamDto> streams;
}
