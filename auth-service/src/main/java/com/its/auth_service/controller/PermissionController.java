package com.its.auth_service.controller;

import com.its.auth_service.configuration.WebConfig;
import com.its.auth_service.model.request.PermissionRequest;
import com.its.auth_service.service.PermissionService;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.response.Response;
import com.its.module.utils.Constants;
import com.its.module.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public Response getAll() {
        return permissionService.getAll();
    }
}