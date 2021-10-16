package com.its.auth_service.service;

import com.its.auth_service.model.request.PermissionRequest;
import com.its.auth_service.model.request.RoleRequest;
import com.its.module.model.response.Response;

public interface PermissionService {
    Response getAll();
}
