package com.its.highway_service.service;

import com.its.highway_service.model.request.SupervisorRequest;
import com.its.highway_service.model.request.UserHighwayRequest;
import com.its.module.model.response.Response;

public interface UserHighwayService {
    Response<?> modify(SupervisorRequest supervisorRequest, Integer authId);
}
