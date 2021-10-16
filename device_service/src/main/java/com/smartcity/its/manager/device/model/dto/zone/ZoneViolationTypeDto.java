package com.smartcity.its.manager.device.model.dto.zone;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ZoneViolationTypeDto {
    @JsonIgnore
    private Integer id;
    private String name;
    private String choiceType;
    private String labelType;
    private List<ZoneTypeDto> zoneTypes;
}
