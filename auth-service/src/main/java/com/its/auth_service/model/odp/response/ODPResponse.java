package com.its.auth_service.model.odp.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ODPResponse <T> {
    Integer resultCode;
    String resultMessage;
    T data;
}
