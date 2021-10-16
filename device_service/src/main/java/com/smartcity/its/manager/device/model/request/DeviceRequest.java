package com.smartcity.its.manager.device.model.request;

import lombok.Data;

@Data
public class DeviceRequest {
    private String name;
    private Integer highwayId;
    private Double latitude;
    private Double longitude;
    private String streamUrl;
    private Integer type;
}
