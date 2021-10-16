package com.smartcity.its.manager.violation.model.request.scp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScpEvidence {
    @JsonProperty("bounding_box")
    private ScpBoundingBox boundingBox;
}
