package com.smartcity.its.manager.device.model.dto.zone;

import javax.validation.constraints.NotNull;

import com.its.module.utils.Constants.FieldError;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ZoneAttrRequest {
    @NotNull(message = FieldError.NULL)
    @EqualsAndHashCode.Include
    private Integer zoneTypeId;
    private Float value;
}
