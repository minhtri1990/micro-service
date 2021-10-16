package com.smartcity.its.manager.device.model.request;

import com.its.module.utils.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeviceConfigRequest {
    @NotNull(message = Constants.FieldError.NULL)
    private String ids;
    @NotNull(message = Constants.FieldError.NULL)
    private List<ConfigRequest> configs;
}
