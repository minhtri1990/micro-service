package com.smartcity.its.manager.device.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.its.module.model.response.BaseResponse;
import com.its.module.utils.Constants.*;
import com.smartcity.its.manager.device.service.DeviceService;

@RestController
@RequestMapping("/devices")
public class DeviceController {
	@Autowired
	DeviceService deviceService;
	
	@GetMapping
	BaseResponse search(@RequestParam Map<String, String> params,
						@RequestParam(value = Pagination.PAGE_REQUEST_PARAM, defaultValue = Pagination.PAGE_DEFAULT) Integer pageNum,
						@RequestParam(value = Pagination.PAGE_SIZE_REQUEST_PARAM, defaultValue = Pagination.PAGE_SIZE_DEFAULT) Integer pageSize,
						Authentication authentication) {
		return deviceService.searchDevices(params, pageNum, pageSize, authentication);
	}
}
