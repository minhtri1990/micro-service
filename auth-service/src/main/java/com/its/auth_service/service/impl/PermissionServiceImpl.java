package com.its.auth_service.service.impl;

import com.its.auth_service.configuration.WebConfig;
import com.its.auth_service.model.dto.PermissionDto;
import com.its.auth_service.model.entity.PermissionEntity;
import com.its.auth_service.model.request.PermissionRequest;
import com.its.auth_service.repository.PermissionRepository;
import com.its.auth_service.service.PermissionService;
import com.its.module.model.exception.NotfoundException;
import com.its.module.model.response.Response;
import com.its.module.utils.GsonUtils;
import com.its.module.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;


    @Override
    public Response getAll() {
        List<PermissionEntity> permissionEntities = permissionRepository.findAll();
        return Response.builder()
                .code(HttpStatus.OK.value())
                .data(GsonUtils.mapObject(permissionEntities, new TypeToken<List<PermissionDto>>(){}.getType()))
                .build();
    }
}
