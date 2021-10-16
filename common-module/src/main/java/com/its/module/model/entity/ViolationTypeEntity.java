package com.its.module.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="violation_type", schema = "its")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViolationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "violation_type_id")
    private Integer id;

    @Column(name = "violation_code", nullable = false, length = 255)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "type")
    private Integer type;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_urgent")
    private Boolean isUrgent;

    @Column(name = "choice_type")
    private String choiceType;

    @Column(name = "label_type")
    private String labelType;

    @Column(name = "english_name")
    private String englishName;
}
