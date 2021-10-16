package com.its.module.model.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
	private Integer id;
	private String ssoId;
	private String userName;
	private String fullName;
	private String email;
	private String phone;
	private Integer gender;
	private String dob;
	private Integer role;
	private LocalDateTime createdDate;
	private String avatar;
	private Set<String> permissions;
}
