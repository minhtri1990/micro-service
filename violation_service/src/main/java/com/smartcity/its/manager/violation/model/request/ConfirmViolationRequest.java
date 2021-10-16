package com.smartcity.its.manager.violation.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants;
import com.its.module.utils.TrimDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class ConfirmViolationRequest {
    @NotNull(message = Constants.FieldError.NULL)
    private Boolean isViolation=false;
    
    @NotNull(message = Constants.FieldError.NULL)
    @Size(max = 1000)
    @JsonDeserialize(using = TrimDeserializer.class)
    private String description;
}
