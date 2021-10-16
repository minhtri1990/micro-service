package com.smartcity.its.manager.violation.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsRequest {
    private String phoneNumber;
    private String message;
    private Integer sourceType;
}
