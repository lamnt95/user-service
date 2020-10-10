package com.user_service.service;

import com.user_service.model.user.User;
import com.user_service.payload.UserIdentityAvailability;
import com.user_service.payload.UserProfile;
import com.user_service.payload.UserSummary;
import com.user_service.payload.request.UserRequest;
import com.user_service.payload.response.ApiResponse;
import com.user_service.security.UserPrincipal;

public interface UserService {
    UserSummary getCurrentUser(UserPrincipal currentUser);

    UserProfile getUserProfile(String username);

    User addNewUser(UserRequest request);

    User updateUser(String username, UserRequest request, UserPrincipal currentUser);

    ApiResponse deleteUser(String username, UserPrincipal currentUser);

    UserIdentityAvailability checkUsernameAvailability(String username);

    UserIdentityAvailability checkEmailAvailability(String email);

    ApiResponse giveAdmin(String username);

    ApiResponse removeAdmin(String username);
}
