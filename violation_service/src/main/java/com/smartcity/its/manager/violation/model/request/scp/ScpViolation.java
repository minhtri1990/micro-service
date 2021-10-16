package com.smartcity.its.manager.violation.model.request.scp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScpViolation {
    private String id;
    private String type;
    @JsonProperty("cameraId")
    private String deviceId;
    private String address;
    private String location;
//    @JsonProperty("license_plate")
    private String licensePlate;
    private Long timestamp;
//    @JsonProperty("vehicle_type")
    private String vehicleType;
//    private ScpEvidence evidence;
//    @JsonProperty("is_violation")
//    private Boolean isViolation;
    private String description;
}

