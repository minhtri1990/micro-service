package com.its.auth_service.model.request;

import com.its.module.utils.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRoleRequest {
    @NotNull(message = Constants.FieldError.NULL)
    private Integer role;
}
