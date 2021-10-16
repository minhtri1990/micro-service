package com.its.auth_service.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants.*;
import com.its.module.utils.TrimDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest {
	@NotNull(message = FieldError.NULL)
	@JsonDeserialize(using = TrimDeserializer.class)
	private String oldPassword;

	@NotNull(message = FieldError.NULL)
	@JsonDeserialize(using = TrimDeserializer.class)
	@Pattern(regexp = Regex.PASSWORD)
	@Size(max = 100, message = FieldError.MAX_LENGTH)
	private String newPassword;
}
