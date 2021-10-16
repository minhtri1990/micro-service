package com.its.highway_service.model.request;

import com.its.module.utils.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class SupervisorRequest {
    @NotNull(message = Constants.FieldError.NULL)
    private Integer userId;
    @NotNull(message = Constants.FieldError.NULL)
    private Set<Integer> highwayIds;
}
