package com.its.auth_service.model.entity;

import com.its.auth_service.utils.OtpType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp", schema = "its")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private Integer id;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private OtpType type;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "otp_code")
    private String code;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}