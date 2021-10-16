package com.smartcity.its.manager.violation.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.its.module.model.response.BaseResponse;
import com.smartcity.its.manager.violation.model.request.warning.WarningConfigRequest;
import com.smartcity.its.manager.violation.service.WarningConfigService;

@RestController
@RequestMapping("/warning-configs")
public class WarningConfigController {
    @Autowired
    private WarningConfigService warningConfigService;

    @GetMapping
    public BaseResponse getById( @RequestParam(value = "type", defaultValue = "0") Integer type, Authentication authentication) {
        return warningConfigService.getByUserIdAndType((Integer) authentication.getPrincipal(), type);
    }

    @PutMapping
    public BaseResponse update(@RequestBody @Valid WarningConfigRequest warningConfigRequest, Authentication authentication) {
        return warningConfigService.update((Integer) authentication.getPrincipal(), warningConfigRequest, authentication);
    }
}
