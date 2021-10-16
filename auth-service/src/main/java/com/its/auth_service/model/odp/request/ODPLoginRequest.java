package com.its.auth_service.model.odp.request;

import lombok.Data;

@Data
public class ODPLoginRequest {
    private String userName;
    private String password;
}
