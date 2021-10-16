package com.its.auth_service.model.odp.request;

import lombok.Data;

import java.util.List;

@Data
public class ODPIdsRequest {
    private List<String> ids;
}
