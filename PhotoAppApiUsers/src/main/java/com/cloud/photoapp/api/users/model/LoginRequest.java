package com.cloud.photoapp.api.users.model;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
