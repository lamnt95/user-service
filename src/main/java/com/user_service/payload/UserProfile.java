package com.user_service.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserProfile {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Instant joinedAt;

    private String phone;

    private String website;

    private Long postCount;
}
