package com.its.auth_service.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFullDto {
    private Integer id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private RoleDto role;
    private String avatar;
    private Boolean isActive;
    private Integer gender;
    private String dob;
    private LocalDateTime createdDate;
}
