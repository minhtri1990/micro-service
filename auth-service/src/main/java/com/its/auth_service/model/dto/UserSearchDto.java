package com.its.auth_service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public interface UserSearchDto {
     Integer getId();
     String getUserName();
     String getFullName();
     String getEmail();
     String getPhone();
     Integer getRole();
     String getAvatar();
     Integer getIsActive();
     LocalDateTime getCreatedDate();
     @JsonIgnore
     Integer getTotal();
}
