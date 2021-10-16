package com.smartcity.its.manager.device.model.request.scp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScpRequest {
    @JsonProperty("camera_id")
    private String cameraId;

    @JsonProperty("camera_tenant_id")
    private String tenantId;
    
    private String token;

    private Object config;
}
