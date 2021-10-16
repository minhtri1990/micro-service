package com.smartcity.its.manager.violation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.its.module.model.response.BaseResponse;
import com.smartcity.its.manager.violation.service.ViolationTypeService;

@RestController
@RequestMapping("/violation-types")
public class ViolationTypeController {
    @Autowired
    private ViolationTypeService violationTypeService;

    @GetMapping
    public BaseResponse getAll(@RequestParam(value = "type", defaultValue = "0") Integer type) {
        return violationTypeService.getAll(type);
    }
}
