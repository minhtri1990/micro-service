package com.its.auth_service.model.entity;

import com.its.auth_service.model.dto.UserSearchDto;
import com.its.module.model.entity.Editable;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "user", schema = "its")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements Editable, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "sso_id", unique = true, nullable = false)
    private String ssoId;

    @Column(name = "username")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String fullName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "dob")
    private String dob;

    @Column(name = "role")
    private Integer role;

    @Column(name = "locked_date")
    private Long lockedDate;

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
