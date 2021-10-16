package com.smartcity.its.manager.violation.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.its.module.model.entity.Editable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "violation", schema = "its")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ViolationEntity implements Editable {
	@Id
	@Column(name = "violation_id")
	private String id;

	@Column(name = "violation_type_id")
	private Integer type;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "license_plate")
	private String licensePlate;

	@Column(name = "vehicle_type")
	private String vehicleType;

	@Column(name = "vehicle_color")
	private String vehicleColor;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "location")
	private String location;

	@Column(name = "status")
	private Integer status;

	@Column(name = "is_violation")
	private Boolean isViolation;

	@Column(name = "description")
	private String description;

	@Column(name = "violation_timestamp")
	private Long timestamp;

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
