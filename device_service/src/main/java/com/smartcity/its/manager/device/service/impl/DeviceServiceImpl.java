package com.smartcity.its.manager.device.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.its.module.model.dto.PaginatedList;
import com.its.module.model.response.Meta;
import com.its.module.model.response.Response;
import com.smartcity.its.manager.device.model.dto.ioc.CameraObjectDto;
import com.smartcity.its.manager.device.repository.DeviceRepository;
import com.smartcity.its.manager.device.service.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService {
	@Autowired
	DeviceRepository deviceRepository;
	
	@Override
	public Response<?> searchDevices(Map<String, String> params, Integer pageNum, Integer pageSize,
			Authentication authentication) {
		if(params.containsKey("deviceId")) params.put("camId", params.get("deviceId"));
		Response<CameraObjectDto> response = deviceRepository.getCameras(params, pageNum, pageSize, authentication);
		return response;
	}
}
