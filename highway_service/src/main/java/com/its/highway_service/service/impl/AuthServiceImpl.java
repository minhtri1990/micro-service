package com.its.highway_service.service.impl;

import com.its.highway_service.repository.HighwayRepository;
import com.its.highway_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private HighwayRepository highwayRepository;

    @Override
    public boolean isPermittedAccessHighway(Integer authId, Integer highwayId) {
        return highwayRepository.isPermittedAccessHighway(authId, highwayId) == 1;
    }
}
