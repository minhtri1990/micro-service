package com.smartcity.its.manager.device.model.dto.zone;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ZoneTypeDto {
    private Integer id;
    private String name;
    private Float max;
    private Float min;
    private String unit;
}
