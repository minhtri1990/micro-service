package com.its.auth_service.model.request;

import com.its.module.model.annotation.NotNull;
import com.its.module.model.annotation.Trim;
import com.its.module.model.annotation.MaxLength;
import lombok.Data;

@Data
public class PermissionRequest {
    @NotNull
    @Trim
    @MaxLength(255)
    private String name;

    @Trim
    @MaxLength(255)
    private String description;
}
