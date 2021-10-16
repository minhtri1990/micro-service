package com.smartcity.its.manager.device.repository;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.its.module.model.response.Response;
import com.smartcity.its.manager.device.model.dto.ioc.CameraDataDto;
import com.smartcity.its.manager.device.model.dto.ioc.CameraObjectDto;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name="ioc", url="${IOC_SERVER}", fallbackFactory = IocFallbackFactory.class)
public interface IocFeignClient {
	@GetMapping("/1.0/get-list-camera")
	Response<CameraDataDto> getCameraById(@RequestHeader("Authorization") String token, @RequestParam("camId") String camId);
	
	@GetMapping("/1.0/get-list-camera")
	Response<CameraDataDto> getCameras(@RequestHeader("Authorization") String token, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);
	
	@GetMapping("/1.0/get-list-camera")
	Response<CameraObjectDto> getCameraObjects(@RequestHeader("Authorization") String token, @RequestParam Map<String, String> params,
			@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);
}

@Component
class IocFallbackFactory implements FallbackFactory<IocFeignClient>{
	@Override
	public IocFeignClient create(Throwable cause) {
		FeignException fe = (FeignException) cause;
		System.out.println("factory" + fe.status());
		//cause.printStackTrace();
		
		return new IocFeignClient() {
			@Override
			public Response<CameraDataDto> getCameraById(String token, String camId) {
				return null;
			}
			
			@Override
			public Response<CameraDataDto> getCameras(String token, Integer pageNum, Integer pageSize) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Response<CameraObjectDto> getCameraObjects(String token, Map<String, String> params, Integer pageNum,
					Integer pageSize) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}