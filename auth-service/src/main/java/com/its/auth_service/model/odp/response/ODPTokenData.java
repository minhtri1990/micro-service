package com.its.auth_service.model.odp.response;

import lombok.Data;

@Data
public class ODPTokenData {
    private String token;
    private Integer expiresIn;
    private String tokenType;
    private ODPUserData userInfo;
}
