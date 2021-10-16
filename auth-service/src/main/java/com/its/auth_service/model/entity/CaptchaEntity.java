package com.its.auth_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "captcha", schema = "its")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaptchaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "captcha_id")
    private Integer id;

    @Column(name = "validate_code")
    private String validateCode;

    @Column(name = "captcha_code")
    private String code;

    @Column(name = "used_times")
    private Integer usedTimes;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
