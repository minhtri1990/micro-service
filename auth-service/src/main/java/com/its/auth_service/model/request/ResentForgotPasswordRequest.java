package com.its.auth_service.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants.*;
import com.its.module.utils.TrimAndLowercaseDeserializer;
import com.its.module.utils.TrimDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResentForgotPasswordRequest {
    @NotNull(message = FieldError.NULL)
    @JsonDeserialize(using = TrimAndLowercaseDeserializer.class)
    private String email;

    @NotNull(message = FieldError.NULL)
    @JsonDeserialize(using = TrimDeserializer.class)
    private String otpToken;
}
