package com.its.auth_service.model.request;

import com.its.module.utils.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TokenRequest {
	@NotNull(message = Constants.FieldError.NULL)
	private String token;

	public String replaceToken() {
		if (token == null) {
			return "";
		}
		return token.replace("Bearer", "").trim();
	}
}
