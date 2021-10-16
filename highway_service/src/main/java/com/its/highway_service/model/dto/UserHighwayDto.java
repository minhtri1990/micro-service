package com.its.highway_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHighwayDto {
    private Integer id;
    private Integer userId;
    private Integer highwayId;
    private LocalDateTime createdDate;
}
