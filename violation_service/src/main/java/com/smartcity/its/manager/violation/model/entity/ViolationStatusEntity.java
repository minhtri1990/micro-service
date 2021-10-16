package com.smartcity.its.manager.violation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "violation_status", schema = "its")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViolationStatusEntity {
    @Id
    @Column(name = "violation_status_id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
