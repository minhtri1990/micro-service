package com.smartcity.its.manager.violation.model.request.warning;


import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.its.module.utils.Constants.FieldError;

import lombok.Data;

@Data
public class WarningConfigItemRequest {
    @NotNull(message = FieldError.NULL)
    private Integer violationTypeId;
    @NotNull(message = FieldError.NULL)
    private Integer sendingType;
    @NotNull(message = FieldError.NULL)
    private List<Integer> sendingMethod;
    @NotNull(message = FieldError.NULL)
    @Max(value = 60, message = FieldError.RANGE_EXCEED)
    private Integer repetition;
}
