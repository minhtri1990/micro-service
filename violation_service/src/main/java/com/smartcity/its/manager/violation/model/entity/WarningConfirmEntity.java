package com.smartcity.its.manager.violation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warning_confirm", schema = "its")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WarningConfirmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warning_confirm_id")
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
