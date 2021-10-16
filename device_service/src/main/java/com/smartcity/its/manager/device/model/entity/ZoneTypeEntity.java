package com.smartcity.its.manager.device.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zone_type", schema = "its")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ZoneTypeEntity {
    @Id
    @Column(name = "zone_type_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "setting_code")
    private String settingCode;

    @Column(name = "violation_type_id")
    private Integer violationTypeId;

    @Column(name = "max")
    private Float max;

    @Column(name = "min")
    private Float min;

    @Column(name = "unit")
    private String unit;
}
