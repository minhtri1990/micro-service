package com.smartcity.its.manager.violation.model.request.scp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScpVehicleLaneConfig {
    @JsonProperty("moto_lane")
    private List<ScpArea> motoLane;

    @JsonProperty("other_lane")
    private List<ScpArea> otherLane;
}
