package com.smartcity.its.manager.device.model.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConfigRequest {
    @NotNull
    private Integer configId;
    @NotNull
    private Boolean value;
}
