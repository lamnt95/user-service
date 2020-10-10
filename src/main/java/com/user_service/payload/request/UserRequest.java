package com.user_service.payload.request;

import lombok.Getter;
import lombok.Setter;
import com.user_service.model.role.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserRequest {
    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String website;

    private Set<Role> roles = new HashSet<>();
}
