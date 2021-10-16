package com.smartcity.its.manager.device.model.dto.zone;

import javax.validation.constraints.NotNull;

import com.its.module.utils.Constants.FieldError;

import lombok.Data;

@Data
public class ZonePointRequest {
    @NotNull(message = FieldError.NULL)
    private Double x;

    @NotNull(message = FieldError.NULL)
    private Double y;
}
