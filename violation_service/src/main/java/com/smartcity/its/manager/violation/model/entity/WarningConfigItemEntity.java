package com.smartcity.its.manager.violation.model.entity;

import com.its.module.model.entity.Editable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warning_config_item", schema = "its")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WarningConfigItemEntity implements Editable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warning_config_item_id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "violation_type_id")
    private Integer violationTypeId;

    @Column(name = "sending_type")
    private Integer sendingType;

    @Column(name = "sending_method")
    private String sendingMethod;

    @Column(name = "repetition")
    private Integer repetition;

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
