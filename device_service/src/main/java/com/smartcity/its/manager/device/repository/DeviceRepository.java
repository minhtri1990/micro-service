package com.smartcity.its.manager.device.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import com.its.module.model.exception.BusinessException;
import com.its.module.model.exception.ForbiddenException;
import com.its.module.model.response.Response;
import com.smartcity.its.manager.device.model.dto.ioc.CameraDataDto;
import com.smartcity.its.manager.device.model.dto.ioc.CameraObjectDto;

@Repository
public class DeviceRepository {
	@Autowired
	private IocFeignClient iocFeignClient;
	
	public boolean isPermitted(String deviceId, Authentication authentication) {
		Response<CameraDataDto> camera = iocFeignClient.getCameraById((String)authentication.getCredentials(), deviceId);
        if(camera.getCode() != 200)
        	throw new BusinessException(camera.getCode(), camera.getMessage(), 1080, "Trạng thái trực tiếp trả về từ IOC get list camera");
        if(camera.getData().getCameras().size() == 0) 
        	throw new ForbiddenException();
        return true;
	}
	
	public List<String> getCameraIds(Authentication authentication) {
		//System.out.println(authentication);
		return iocFeignClient.getCameras((String) authentication.getCredentials(), 1, 2000)
			.getData()
			.getCameras().stream()
			.map(camera -> camera.getId())
			.collect(Collectors.toList());
	}
	
	public Response<CameraObjectDto> getCameras(Map<String, String> params, Integer pageNum, Integer pageSize, Authentication authentication) {
		return iocFeignClient.getCameraObjects((String) authentication.getCredentials(), params, pageNum, pageSize);
	}
}
