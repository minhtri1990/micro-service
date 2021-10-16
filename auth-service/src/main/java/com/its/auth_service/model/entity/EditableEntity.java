package com.its.auth_service.model.entity;

import com.its.module.model.entity.Editable;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class EditableEntity implements Editable {
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
