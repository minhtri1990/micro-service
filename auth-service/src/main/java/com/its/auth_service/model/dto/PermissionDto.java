package com.its.auth_service.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermissionDto {
    private Integer id;
    private String name;
    private String description;
}
