package com.its.auth_service.model.odp.request;

import lombok.Data;

@Data
public class ODPChangePasswordRequest {
    private String userName;
    private String oldPassword;
    private String newPassword;
}
