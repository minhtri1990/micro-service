package com.smartcity.its.manager.device.model.dto.ioc;

import java.util.List;

import com.its.module.model.response.Meta;

import lombok.Data;

@Data
public class CameraObjectDto {
	private List<Object> cameras;
	private Meta meta;
}
	