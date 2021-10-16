package com.its.module.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "highway", schema = "its")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HighwayEntity implements Editable {
	@Column(name = "highway_id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "highway_name", nullable = false)
	private String name;

	@Column(name = "tnt_id")
	private String tntId;

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
