package com.user_service.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class RegisterRequest {
    private String firstName;

    private String lastName;

    private String username;

    @Email
    private String email;

    private String password;
}
