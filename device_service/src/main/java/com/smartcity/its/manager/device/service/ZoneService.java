package com.smartcity.its.manager.device.service;

import com.its.module.model.response.BaseResponse;
import com.smartcity.its.manager.device.model.dto.zone.ZoneCreateRequest;
import com.smartcity.its.manager.device.model.dto.zone.ZoneInfoRequest;

import java.util.Map;

import org.springframework.security.core.Authentication;

public interface ZoneService {
    BaseResponse createNew(ZoneCreateRequest zoneCreateRequest, Authentication authentication);
    BaseResponse getOne(Integer id, Authentication authentication);
    BaseResponse getAll(Map<String, String> params, Authentication authentication);
    BaseResponse updateOne(Integer id, ZoneInfoRequest zoneInfoRequest, Authentication authentication);
    BaseResponse deleteOne(Integer id, Authentication authentication);
    BaseResponse stopScp(String deviceId);
    BaseResponse stopAllScp();

    BaseResponse getZoneTypes();
}
