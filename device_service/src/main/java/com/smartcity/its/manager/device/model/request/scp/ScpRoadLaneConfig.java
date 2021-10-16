package com.smartcity.its.manager.device.model.request.scp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScpRoadLaneConfig {
    @JsonProperty("left_lane")
    private List<ScpArea> leftLane;

    @JsonProperty("right_lane")
    private List<ScpArea> rightLane;
}
