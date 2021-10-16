package com.its.highway_service.service;

import com.its.highway_service.model.request.HighwayRequest;
import com.its.highway_service.model.request.UserHighwayRequest;
import com.its.module.model.response.Response;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;
import java.util.Set;

public interface HighwayService {
    Response<?> getAllHighway(Map<String, String> params, Integer userId, boolean isAdmin, Integer page, Integer pageSize);
    Response<?> getHighwayById(Integer id, Integer authId);
    Response<?> createHighway(HighwayRequest highwayRequest, Integer authId);
    Response<?> changeHighwayInfo(Integer highwayId, HighwayRequest highwayRequest, Integer authId);
    Response<?> deleteHighway(Integer id, Integer authId);
}
