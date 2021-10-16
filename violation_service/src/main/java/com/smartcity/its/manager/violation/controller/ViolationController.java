package com.smartcity.its.manager.violation.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.its.module.model.exception.ForbiddenException;
import com.its.module.model.response.BaseResponse;
import com.its.module.utils.Constants.Pagination;
import com.smartcity.its.manager.violation.model.request.ConfirmViolationRequest;
import com.smartcity.its.manager.violation.model.request.scp.ScpWrapperRequest;
import com.smartcity.its.manager.violation.service.ViolationService;

@RestController
@RequestMapping("/violations")
public class ViolationController {
    @Autowired
    private ViolationService violationService;

    @Value("${WARNING_DAILY_REPORT_APIKEY}")
    private String WARNING_DAILY_REPORT_APIKEY;
    @Value("${SCP_APIKEY}")
    private String SCP_APIKEY;

    @PostMapping("/scp")
    public BaseResponse pushViolationFromScp(@RequestBody ScpWrapperRequest scpWrapperRequest) {
        return violationService.receiveViolationFromScp(scpWrapperRequest.getEntities());
    }

    @GetMapping("/extract")
    public ResponseEntity extract(@RequestParam Map<String, String> requestParams,
                                  @RequestParam(value = Pagination.PAGE_REQUEST_PARAM, defaultValue = Pagination.PAGE_DEFAULT) Integer page,
                                  @RequestParam(value = Pagination.PAGE_SIZE_REQUEST_PARAM, defaultValue = Pagination.PAGE_SIZE_DEFAULT) Integer pageSize,
                                  Authentication authentication) {
        return violationService.extract(requestParams, page, pageSize, authentication);
    }

    @GetMapping
    public BaseResponse search(@RequestParam Map<String, String> requestParams,
                               @RequestParam(value = Pagination.PAGE_REQUEST_PARAM, defaultValue = Pagination.PAGE_DEFAULT) Integer page,
                               @RequestParam(value = Pagination.PAGE_SIZE_REQUEST_PARAM, defaultValue = Pagination.PAGE_SIZE_DEFAULT) Integer pageSize,
                               Authentication authentication) {
        return violationService.search(requestParams, page, pageSize, authentication);
    }

    @GetMapping("/{id}")
    public BaseResponse getOne(@PathVariable("id") String id, Authentication authentication) {
        return violationService.getOne(id, authentication);
    }

    @PutMapping("/{id}/confirms")
    public BaseResponse confirm(@PathVariable("id") String id,
                                @RequestBody @Valid ConfirmViolationRequest confirmNotViolationRequest,
                                Authentication authentication) {
        return violationService.confirmNotViolated(id, confirmNotViolationRequest, authentication);
    }

    @GetMapping("/send-daily")
    public BaseResponse sendDaily(@RequestHeader("apikey") String apikey) {
        if(!WARNING_DAILY_REPORT_APIKEY.equals(apikey)) throw new ForbiddenException();
        return violationService.sendDaily();
    }
}
