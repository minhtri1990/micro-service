package com.its.auth_service.model.odp.request;

import lombok.Data;

@Data
public class ODPUserStatusRequest {
    private String userId;
    private Boolean enabled;
}
