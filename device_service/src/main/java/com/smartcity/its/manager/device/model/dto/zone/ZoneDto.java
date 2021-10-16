package com.smartcity.its.manager.device.model.dto.zone;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ZoneDto {
    private Integer id;
    private String deviceId;
    private Set<ZoneAttrDto> zoneAttrs;
    private String name;
    private List<ZonePointDto> coords;
    private LocalDateTime createdDate;
}
