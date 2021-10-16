package com.smartcity.its.manager.device.model.entity;

import com.its.module.model.entity.Editable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "zone_attr", schema = "its")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneAttrEntity implements Editable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "zone_id")
    private Integer zoneId;

    @Column(name = "zone_type_id")
    private Integer zoneTypeId;

    @Column(name = "value")
    private Float value;

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
