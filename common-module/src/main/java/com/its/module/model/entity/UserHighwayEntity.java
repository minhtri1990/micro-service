package com.its.module.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_highway", schema = "its")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHighwayEntity implements Editable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_highway_id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "highway_id")
    private Integer highwayId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private Integer createdBy;
}
