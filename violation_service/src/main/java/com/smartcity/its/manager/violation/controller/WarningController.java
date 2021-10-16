package com.smartcity.its.manager.violation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.module.model.response.BaseResponse;
import com.smartcity.its.manager.violation.service.WarningService;

@RestController
@RequestMapping("/warnings")
public class WarningController {
    @Autowired
    private WarningService warningService;

    @Value("${WARNING_DAILY_REPORT_APIKEY}")
    private String APIKEY;

//    @GetMapping("/send-daily")
//    public BaseResponse sendDaily(@RequestHeader("apikey") String apikey) {
//        if(!APIKEY.equals(apikey))
//            throw new ForbiddenException();
//        return warningService.sendDaily();	
//    }

    @GetMapping("/stop-report")
    public BaseResponse stopReport(Authentication authentication) {
        return warningService.stopReport((Integer) authentication.getPrincipal());
    }
}
