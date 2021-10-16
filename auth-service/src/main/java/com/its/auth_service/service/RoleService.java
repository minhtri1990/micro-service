package com.its.auth_service.service;

import com.its.auth_service.model.request.RolePermissionRequest;
import com.its.auth_service.model.request.RoleRequest;
import com.its.module.model.response.Response;

public interface RoleService {
    Response getAll();
    Response addRole(RoleRequest roleRequest, Integer authId);
    Response deleteRole(Integer roleId, Integer authId);
    Response updateRole(Integer roleId, RoleRequest roleRequest, Integer authId);
    Response addPermission(RolePermissionRequest rolePermissionRequest, Integer authId);
    Response removePermission(RolePermissionRequest rolePermissionRequest, Integer authId);
}
