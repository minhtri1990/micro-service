package com.its.auth_service.model.odp.request;

import lombok.Data;

@Data
public class ODPResetPasswordRequest {
    private String userName;
    private String newPassword;
}
