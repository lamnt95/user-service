package com.user_service.service.impl;

import com.user_service.exception.AccessDeniedException;
import com.user_service.exception.AppException;
import com.user_service.exception.BadRequestException;
import com.user_service.exception.ResourceNotFoundException;
import com.user_service.model.role.Role;
import com.user_service.model.role.RoleName;
import com.user_service.model.user.User;
import com.user_service.payload.UserIdentityAvailability;
import com.user_service.payload.UserProfile;
import com.user_service.payload.UserSummary;
import com.user_service.payload.request.UserRequest;
import com.user_service.payload.response.ApiResponse;
import com.user_service.repository.RoleRepository;
import com.user_service.repository.UserRepository;
import com.user_service.security.UserPrincipal;
import com.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final String USERNAME_ALREADY_TAKEN = "Username is already taken.";
    private static final String EMAIL_ALREADY_TAKEN = "Email is already taken.";
    private static final String USER_ROLE_NOT_SET = "User role not set";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName());
    }

    @Override
    public UserProfile getUserProfile(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        UserProfile userProfile = new UserProfile();

        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setEmail(user.getEmail());
        userProfile.setJoinedAt(user.getCreatedAt());
        userProfile.setPhone(user.getPhone());
        userProfile.setWebsite(user.getWebsite());

        return userProfile;
    }

    @Override
    public User addNewUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            ApiResponse response = new ApiResponse(Boolean.FALSE, USERNAME_ALREADY_TAKEN);
            throw new BadRequestException(response);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            ApiResponse response = new ApiResponse(Boolean.FALSE, EMAIL_ALREADY_TAKEN);
            throw new BadRequestException(response);
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setWebsite(request.getWebsite());

        Set<Role> roles = new HashSet<>();

        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        user.setRoles(roles);

        User newUser = userRepository.save(user);

        return newUser;
    }

    @Override
    public User updateUser(String username, UserRequest request, UserPrincipal currentUser) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (user.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setWebsite(request.getWebsite());

            User updateUser = userRepository.save(user);

            return updateUser;
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + username);
        throw new AccessDeniedException(apiResponse);
    }

    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (user.getId().equals(currentUser.getId()) || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            userRepository.deleteById(user.getId());

            return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + username);
        throw new AccessDeniedException(apiResponse);
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailable = userRepository.existsByUsername(username);

        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(@Email String email) {
        Boolean isAvailable = userRepository.existsByEmail(email);

        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public ApiResponse giveAdmin(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        Set<Role> roles = new HashSet<>();

        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));

        user.setRoles(roles);
        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, "You gave ADMIN role to user: " + username);
    }

    @Override
    public ApiResponse removeAdmin(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Set<Role> roles = new HashSet<>();

        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        user.setRoles(roles);
        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + username);
    }
}
