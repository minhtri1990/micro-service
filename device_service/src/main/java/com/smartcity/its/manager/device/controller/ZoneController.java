package com.smartcity.its.manager.device.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.its.module.model.response.BaseResponse;
import com.smartcity.its.manager.device.model.dto.zone.ZoneCreateRequest;
import com.smartcity.its.manager.device.model.dto.zone.ZoneInfoRequest;
import com.smartcity.its.manager.device.service.ZoneService;

@RestController
@RequestMapping("/zones")
public class ZoneController {
    @Autowired
    private ZoneService zoneService;

    @GetMapping("/{id}")
    private BaseResponse getById(@PathVariable("id") Integer id, Authentication authentication) {
        return zoneService.getOne(id, authentication);
    }

    @PostMapping
    private BaseResponse createNew(@RequestBody @Valid ZoneCreateRequest zoneCreateRequest,
                                   Authentication authentication) {
        return zoneService.createNew(zoneCreateRequest, authentication);
    }

    @GetMapping
    private BaseResponse getAll(@RequestParam Map<String, String> params,
                                Authentication authentication) {
        return zoneService.getAll(params, authentication);
    }

    @PutMapping("/{id}")
    private BaseResponse update(@PathVariable("id") Integer id,
                                @RequestBody @Valid ZoneInfoRequest zoneInfoRequest,
                                Authentication authentication) {
        return zoneService.updateOne(id, zoneInfoRequest, authentication);
    }

    @DeleteMapping("/{id}")
    private BaseResponse delete(@PathVariable("id") Integer id,
                                Authentication authentication) {
        return zoneService.deleteOne(id, authentication);
    }

    @GetMapping("/types")
    private BaseResponse getTypes() {
        return zoneService.getZoneTypes();
    }
}
