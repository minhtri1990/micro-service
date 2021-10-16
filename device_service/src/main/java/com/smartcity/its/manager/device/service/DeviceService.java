package com.smartcity.its.manager.device.service;

import java.util.Map;

import org.springframework.security.core.Authentication;

import com.its.module.model.response.Response;

public interface DeviceService {
	Response<?> searchDevices(Map<String, String> params, Integer pageNum, Integer pageSize, Authentication authentication);
}
