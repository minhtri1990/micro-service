package com.its.auth_service.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants.FieldError;
import com.its.module.utils.Constants.Regex;
import com.its.module.utils.TrimAndLowercaseDeserializer;
import com.its.module.utils.TrimDeserializer;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserInfoRequest {
    @NotNull(message = FieldError.NULL)
    @Size(max = 100, message = FieldError.MAX_LENGTH)
    @Email(message = FieldError.NOT_VALID)
    @JsonDeserialize(using = TrimAndLowercaseDeserializer.class)
    private String email;

    @NotNull(message = FieldError.NULL)
    @Size(max = 255, message = FieldError.MAX_LENGTH)
    @JsonDeserialize(using = TrimDeserializer.class)
    private String fullName;

    @NotNull(message = FieldError.NULL)
    @Pattern(regexp = Regex.PHONE, message = FieldError.NOT_VALID)
    @JsonDeserialize(using = TrimDeserializer.class)
    private String phone;

    @NotNull(message = FieldError.NULL)
    @Max(value = 3, message = FieldError.NOT_VALID)
    @Min(value = 1, message = FieldError.NOT_VALID)
    private Integer gender;

    @JsonDeserialize(using = TrimDeserializer.class)
    private String dob;
}
