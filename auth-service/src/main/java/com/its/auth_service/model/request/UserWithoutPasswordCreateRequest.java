package com.its.auth_service.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.its.module.utils.Constants.*;
import com.its.module.utils.TrimAndLowercaseDeserializer;
import com.its.module.utils.TrimDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithoutPasswordCreateRequest {
    @NotNull(message = FieldError.NULL)
    @JsonDeserialize(using = TrimAndLowercaseDeserializer.class)
    @Size(max = 100, message = FieldError.MAX_LENGTH)
    private String userName;

    @NotNull(message = FieldError.NULL)
    @JsonDeserialize(using = TrimAndLowercaseDeserializer.class)
    @Size(max = 100)
    @Email(message = FieldError.NOT_VALID)
    private String email;

    @NotNull(message = FieldError.NULL)
    @JsonDeserialize(using = TrimDeserializer.class)
    @Size(max = 255, message = FieldError.MAX_LENGTH)
    private String fullName;

    @NotNull(message = FieldError.NULL)
    @JsonDeserialize(using = TrimDeserializer.class)
    @Pattern(regexp = "^0([0-9]){8,11}$", message = FieldError.NOT_VALID)
    private String phone;

    @Max(value = 3, message = FieldError.NOT_VALID)
    @Min(value = 1, message = FieldError.NOT_VALID)
    @NotNull(message = FieldError.NULL)
    private Integer gender;

    @JsonDeserialize(using = TrimDeserializer.class)
    private String dob;

    @NotNull(message = FieldError.NULL)
    private Integer role;
}
