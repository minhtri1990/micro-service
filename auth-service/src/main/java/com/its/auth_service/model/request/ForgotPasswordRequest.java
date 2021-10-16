package com.its.auth_service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants.*;
import com.its.module.utils.TrimAndLowercaseDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {
	@NotNull(message = FieldError.NULL)
	@Size(max = 100, message = FieldError.MAX_LENGTH)
	@JsonDeserialize(using = TrimAndLowercaseDeserializer.class)
	private String email;

	@NotNull(message = FieldError.NULL)
	@Valid
	@JsonProperty("captcha")
	private CaptchaRequest captcha;
}
