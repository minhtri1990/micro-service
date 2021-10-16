package com.smartcity.its.manager.violation.model.dto.warning_config;

import com.smartcity.its.manager.violation.model.entity.WarningConfigEntity;
import lombok.Data;

import java.util.List;

@Data
public class WarningConfigDto {
    private Integer id;
    private Integer type;
    private String phones;
    private String emails;
    private List<WarningConfigItemDto> items;
}
