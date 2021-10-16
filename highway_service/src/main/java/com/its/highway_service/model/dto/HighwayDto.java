package com.its.highway_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HighwayDto {
    private Integer id;
    private String name;
    private String tntId;
    private Integer numDevices;
    private LocalDateTime createdDate;
}
