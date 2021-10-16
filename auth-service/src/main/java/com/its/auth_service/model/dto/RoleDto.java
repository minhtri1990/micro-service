package com.its.auth_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
    private Integer id;
    private String name;
    private String description;
//    private LocalDateTime createdDate;
}
