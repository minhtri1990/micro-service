package com.its.highway_service.model.request;

import com.its.module.model.annotation.NotNull;
import lombok.Data;

@Data
public class UserHighwayRequest {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer highwayId;
}
