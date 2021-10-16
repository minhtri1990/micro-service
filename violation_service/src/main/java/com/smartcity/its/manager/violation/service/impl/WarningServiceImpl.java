package com.smartcity.its.manager.violation.service.impl;

import com.its.module.model.response.BaseResponse;
import com.its.module.model.response.Response;
import com.smartcity.its.manager.violation.configuration.WebConfig;
import com.smartcity.its.manager.violation.model.entity.WarningConfirmEntity;
import com.smartcity.its.manager.violation.repository.WarningConfirmRepository;
import com.smartcity.its.manager.violation.service.WarningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WarningServiceImpl implements WarningService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarningServiceImpl.class);

    @Autowired
    private WarningConfirmRepository warningConfirmRepository;

    @Override
    public BaseResponse stopReport(Integer userId) {
        WarningConfirmEntity confirm = WarningConfirmEntity.builder()
                .userId(userId)
                .createdDate(LocalDateTime.now())
                .build();
        warningConfirmRepository.save(confirm);
        return Response.builder()
                .code(200)
                .message(WebConfig.translate("process_success"))
                .build();
    }
}
