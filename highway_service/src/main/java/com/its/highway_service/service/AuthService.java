package com.its.highway_service.service;

public interface AuthService {
    boolean isPermittedAccessHighway(Integer authId, Integer highwayId);
}
