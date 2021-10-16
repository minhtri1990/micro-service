package com.smartcity.its.manager.device.model.dto.ioc;

import java.util.List;

import com.its.module.model.response.Meta;

import lombok.Data;

@Data
public class CameraDataDto {
	private List<CameraDto> cameras;
	private Meta meta;
}
