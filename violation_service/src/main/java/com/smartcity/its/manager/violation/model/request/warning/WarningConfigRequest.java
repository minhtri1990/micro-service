package com.smartcity.its.manager.violation.model.request.warning;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.its.module.utils.Constants.FieldError;

import lombok.Data;

@Data
public class WarningConfigRequest {
    @NotNull(message = FieldError.NULL)
    private Integer type;
    @NotNull(message = FieldError.NULL)
    @Size(max = 1000, message = FieldError.MAX_LENGTH)
    private String phones;
    @NotNull(message = FieldError.NULL)
    @Size(max = 1000, message = FieldError.MAX_LENGTH)
    private String emails;
    @NotNull(message = FieldError.NULL)
    @Valid
    private List<WarningConfigItemRequest> items;
}
