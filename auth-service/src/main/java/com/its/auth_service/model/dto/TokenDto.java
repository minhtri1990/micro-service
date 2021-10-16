package com.its.auth_service.model.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String token;
    private Integer expiresIn;
    private String tokenType;
}
