package com.smartcity.its.manager.device.model.request.scp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScpConfig {
    @JsonProperty("vehicle_lane_config")
    private ScpVehicleLaneConfig vehicleLaneConfig;

    @JsonProperty("road_lane_config")
    private ScpRoadLaneConfig roadLaneConfig;
}
