package com.smartcity.its.manager.violation.model.request.scp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScpArea implements Cloneable {
    @JsonProperty("area_id")
    private String areaId;

    private List<ScpPoint> poly;

    private Float value;

    @Override
    public ScpArea clone() {
        try {
            return (ScpArea) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("clone not support");
        }
    }
}
