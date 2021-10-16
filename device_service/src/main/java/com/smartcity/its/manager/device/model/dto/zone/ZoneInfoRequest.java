package com.smartcity.its.manager.device.model.dto.zone;
import com.its.module.utils.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ZoneInfoRequest {
    @Size(max = 255, message = Constants.FieldError.MAX_LENGTH)
    private String name;

    @NotNull(message = Constants.FieldError.NULL)
    private List<ZonePointRequest> coords;
}
