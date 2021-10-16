package com.its.auth_service.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants.*;
import com.its.module.utils.TrimDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RoleRequest {
    @NotNull(message = FieldError.NULL)
    @Size(max = 50, message = FieldError.MAX_LENGTH)
    @JsonDeserialize(using = TrimDeserializer.class)
    private String name;

    @Size(max = 255, message = FieldError.MAX_LENGTH)
    @JsonDeserialize(using = TrimDeserializer.class)
    private String description;
}
