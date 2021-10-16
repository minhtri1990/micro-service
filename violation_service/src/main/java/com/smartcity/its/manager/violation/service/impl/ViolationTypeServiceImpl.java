package com.smartcity.its.manager.violation.service.impl;

import java.util.List;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.its.module.model.entity.ViolationTypeEntity;
import com.its.module.model.response.Response;
import com.its.module.utils.GsonUtils;
import com.smartcity.its.manager.violation.model.dto.ViolationTypeDto;
import com.smartcity.its.manager.violation.repository.ViolationTypeRepository;
import com.smartcity.its.manager.violation.service.ViolationTypeService;

@Service
public class ViolationTypeServiceImpl implements ViolationTypeService {
    @Autowired
    private ViolationTypeRepository violationTypeRepository;

    @Override
    public Response getAll(Integer type) {
        List<ViolationTypeEntity> violationTypeEntities = violationTypeRepository.findByTypeAndIsActiveIsTrue(type);
        return Response.builder()
                .code(HttpStatus.OK.value())
                .data(GsonUtils.mapObject(violationTypeEntities, new TypeToken<List<ViolationTypeDto>>(){}.getType()))
                .build();
    }
}
