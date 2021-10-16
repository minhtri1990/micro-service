package com.smartcity.its.manager.violation.model.entity;

import com.its.module.model.entity.Editable;
import com.its.module.model.entity.Removable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "warning_config", schema = "its")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@IdClass(WarningConfigId.class)
public class WarningConfigEntity implements Removable {
    @Id
    @Column(name = "warning_config_id")
    private Integer id;

    @Id
    @Column(name = "type")
    private Integer type;

    @Column(name = "phones", length = 1000)
    private String phones;

    @Column(name = "emails", length = 1000)
    private String emails;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;
}

@AllArgsConstructor
@NoArgsConstructor
class WarningConfigId implements Serializable {
    private Integer id;
    private Integer type;
}
