package com.its.auth_service.controller;

import com.its.auth_service.model.request.RolePermissionRequest;
import com.its.auth_service.model.request.RoleRequest;
import com.its.auth_service.service.RoleService;
import com.its.module.model.response.Response;
import com.its.module.utils.Constants.*;
import com.its.module.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;

    @GetMapping
    public Response getAll() {
        return roleService.getAll();
    }

    @PostMapping
    public Response createNew(@RequestHeader(AuthHeader.USER_ID) Integer authId,
                              @RequestBody @Valid RoleRequest roleRequest) {
        return roleService.addRole(roleRequest, authId);
    }

    @DeleteMapping("/{role_id}")
    public Response deleteOne(@RequestHeader(AuthHeader.USER_ID) Integer authId,
                              @PathVariable("role_id") Integer roleId) {
        return roleService.deleteRole(roleId, authId);
    }

    @PutMapping("/{role_id}")
    public Response updateRole(@PathVariable("role_id") Integer roleId,
                               @RequestBody @Valid RoleRequest roleRequest,
                               @RequestHeader(AuthHeader.USER_ID) Integer authId) {
        return roleService.updateRole(roleId, roleRequest, authId);
    }

    @PostMapping("/permissions")
    public Response addPermission(@RequestBody @Valid RolePermissionRequest rolePermissionRequest,
                                  @RequestHeader(AuthHeader.USER_ID) Integer authId) {
        return roleService.addPermission(rolePermissionRequest, authId);
    }

    @DeleteMapping("/permissions")
    public Response removePermission(@RequestBody @Valid RolePermissionRequest rolePermissionRequest,
                                     @RequestHeader(AuthHeader.USER_ID) Integer authId) {
        return roleService.removePermission(rolePermissionRequest, authId);
    }
}
