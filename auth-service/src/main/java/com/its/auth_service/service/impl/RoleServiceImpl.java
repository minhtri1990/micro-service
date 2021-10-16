package com.its.auth_service.service.impl;

import com.its.auth_service.configuration.WebConfig;
import com.its.auth_service.model.dto.RoleDto;
import com.its.auth_service.model.entity.RoleEntity;
import com.its.auth_service.model.entity.RolePermissionEntity;
import com.its.auth_service.model.request.RolePermissionRequest;
import com.its.auth_service.model.request.RoleRequest;
import com.its.auth_service.repository.RolePermissionRepository;
import com.its.auth_service.repository.RoleRepository;
import com.its.auth_service.service.RoleService;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.exception.NotfoundException;
import com.its.module.model.response.Response;
import com.its.module.utils.GsonUtils;
import com.its.module.utils.ObjectUtils;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    
    @Override
    public Response getAll() {
        List<RoleEntity> roleEntities = roleRepository.findByIsActiveIsTrue();
        return Response.builder()
                .code(HttpStatus.OK.value())
                .data(GsonUtils.mapObject(roleEntities, new TypeToken<List<RoleDto>>(){}.getType()))
                .build();
    }

    @Override
    public Response addRole(RoleRequest roleRequest, Integer authId) {
        RoleEntity roleEntity = GsonUtils.mapObject(roleRequest, RoleEntity.class);
        ObjectUtils.setCreateHistory(roleEntity, authId);
        roleEntity = roleRepository.save(roleEntity);
        return Response.builder()
                .code(HttpStatus.CREATED.value())
                .message(WebConfig.translate("process_success"))
                .data(GsonUtils.mapObject(roleEntity, RoleDto.class))
                .build();
    }

    @Override
    public Response deleteRole(Integer roleId, Integer authId) {
        RoleEntity roleEntity = roleRepository.findByIdAndIsActiveIsTrue(roleId);
        if(roleEntity == null)
            throw new NotfoundException(WebConfig.translate("role.not_found"));
        roleEntity.setIsActive(false);
        ObjectUtils.setModifyHistory(roleEntity, authId);
        roleRepository.save(roleEntity);
        return Response.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message(WebConfig.translate("process_success"))
                .build();
    }

    @Override
    public Response updateRole(Integer roleId, RoleRequest roleRequest, Integer authId) {
        RoleEntity roleEntity = roleRepository.findByIdAndIsActiveIsTrue(roleId);
        if(roleEntity == null)
            throw new NotfoundException(WebConfig.translate("not_found"));
        ObjectUtils.fillData(roleEntity, roleRequest);
        roleEntity = roleRepository.save(roleEntity);
        return Response.builder()
                .code(HttpStatus.OK.value())
                .message(WebConfig.translate("update_success"))
                .data(GsonUtils.mapObject(roleEntity, RoleDto.class))
                .build();
    }

    @Override
    public Response addPermission(RolePermissionRequest rolePermissionRequest, Integer authId) {
        Integer roleId = rolePermissionRequest.getRoleId();
        Integer permissionId = rolePermissionRequest.getPermissionId();
        if(rolePermissionRepository.findByRoleIdAndPermissionIdAndIsActiveIsTrue(roleId, permissionId) != null)
            throw new BadRequestException(WebConfig.translate("role_permission.exist"));
        RolePermissionEntity rolePermissionEntity = RolePermissionEntity.builder()
                .roleId(roleId)
                .permissionId(permissionId)
                .isActive(true)
                .build();
        ObjectUtils.setCreateHistory(rolePermissionEntity, authId);
        rolePermissionRepository.save(rolePermissionEntity);
        return Response.builder()
                .code(HttpStatus.CREATED.value())
                .message(WebConfig.translate("process_success"))
                .build();
    }

    @Override
    public Response removePermission(RolePermissionRequest rolePermissionRequest, Integer authId) {
        Integer roleId = rolePermissionRequest.getRoleId();
        Integer permissionId = rolePermissionRequest.getPermissionId();
        RolePermissionEntity rolePermissionEntity =
                rolePermissionRepository.findByRoleIdAndPermissionIdAndIsActiveIsTrue(roleId, permissionId);
        if(rolePermissionEntity == null)
            throw new NotfoundException(WebConfig.translate("role_permission.not_found"));
        rolePermissionEntity.setIsActive(false);
        ObjectUtils.setModifyHistory(rolePermissionEntity, authId);
        rolePermissionRepository.save(rolePermissionEntity);
        return Response.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message(WebConfig.translate("process_success"))
                .build();
    }
}
