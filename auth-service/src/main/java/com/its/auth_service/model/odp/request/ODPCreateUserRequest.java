package com.its.auth_service.model.odp.request;

import lombok.Data;

@Data
public class ODPCreateUserRequest {
    private String userName;
    private String password;
//    private String email;
//    private String firstName;
//    private String lastName;
    private Boolean enabled;
}
