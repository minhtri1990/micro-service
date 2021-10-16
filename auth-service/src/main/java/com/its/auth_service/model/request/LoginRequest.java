package com.its.auth_service.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants;
import com.its.module.utils.TrimAndLowercaseDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class LoginRequest {
	@NotNull(message = Constants.FieldError.NULL)
	@JsonDeserialize(using = TrimAndLowercaseDeserializer.class)
	private String userName;

	@NotNull(message = Constants.FieldError.NULL)
	private String password;
}