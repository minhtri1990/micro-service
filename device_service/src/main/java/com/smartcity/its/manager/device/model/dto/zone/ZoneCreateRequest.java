package com.smartcity.its.manager.device.model.dto.zone;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants.*;
import com.its.module.utils.TrimDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
public class ZoneCreateRequest {
    @NotNull(message = FieldError.NULL)
    @JsonDeserialize(using = TrimDeserializer.class)
    private String deviceId;

    @Size(max = 255, message = FieldError.MAX_LENGTH)
    @JsonDeserialize(using = TrimDeserializer.class)
    private String name;

    @NotNull(message = FieldError.NULL)
    private Set<ZoneAttrRequest> zoneAttrs;

    @NotNull(message = FieldError.NULL)
    private List<ZonePointRequest> coords;
}
