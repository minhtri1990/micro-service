package com.smartcity.its.manager.device.model.entity;

import com.its.module.model.entity.Editable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "zone", schema = "its")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneEntity implements Editable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Integer id;

    @Column(name = "device_id")
    private String deviceId;


    @Column(name = "zone_name")
    private String name;

    @Column(name = "coords")
    private String coords;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;
}
