package com.its.auth_service.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants.*;
import com.its.module.utils.TrimAndLowercaseDeserializer;
import com.its.module.utils.TrimDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordRequest {
	@NotNull(message = FieldError.NULL)
	@JsonDeserialize(using = TrimAndLowercaseDeserializer.class)
	private String email;

	@NotNull(message = FieldError.NULL)
	@JsonDeserialize(using = TrimDeserializer.class)
	private String code;

	@NotNull(message = FieldError.NULL)
	@Size(max = 100, message = FieldError.MAX_LENGTH)
	@Pattern(regexp = Regex.PASSWORD, message = FieldError.NOT_VALID)
	@JsonDeserialize(using = TrimDeserializer.class)
	private String password;
}
