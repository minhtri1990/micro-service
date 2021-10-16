package com.its.auth_service.model.request;

import com.its.module.utils.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RolePermissionRequest {
    @NotNull(message = Constants.FieldError.NULL)
    Integer roleId;

    @NotNull(message = Constants.FieldError.NULL)
    Integer permissionId;
}
