package com.smartcity.its.manager.violation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warning_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarningLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warning_log_id")
    private Integer id;

    @Column(name = "violation_id")
    private String violationId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "sending_type")
    private Integer sendingType;

    @Column(name = "sending_method")
    private Integer sendingMethod;

    @Column(name = "sending_destination")
    private String sendingDestination;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
