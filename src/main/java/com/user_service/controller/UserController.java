package com.user_service.controller;

import io.swagger.annotations.ApiOperation;
import com.user_service.model.user.User;
import com.user_service.payload.UserIdentityAvailability;
import com.user_service.payload.UserProfile;
import com.user_service.payload.UserSummary;
import com.user_service.payload.request.UserRequest;
import com.user_service.payload.response.ApiResponse;
import com.user_service.security.CurrentUser;
import com.user_service.security.UserPrincipal;
import com.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("Get logged in user profile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = userService.getCurrentUser(currentUser);

        return new ResponseEntity<>(userSummary, HttpStatus.OK);
    }

    @ApiOperation("Get user profile by username")
    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String username) {
        UserProfile userProfile = userService.getUserProfile(username);

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @ApiOperation("Add user (Only for admins)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> addNewUser(@RequestBody UserRequest request) {
        User user = userService.addNewUser(request);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @ApiOperation("Update user (If profile belongs to logged in user or logged in user is admin")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody UserRequest request, @CurrentUser UserPrincipal currentUser) {
        User updateUser = userService.updateUser(username, request, currentUser);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @ApiOperation("Delete user (For logged in user or admin)")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String username, @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = userService.deleteUser(username, currentUser);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //    GET    | /api/users/checkUsernameAvailability | Check if username is available to register
    @ApiOperation("Check if username is available to register")
    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(@RequestParam String username) {
        UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(username);

        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    @ApiOperation("Check if email is available to register")
    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(@RequestParam @Email String email) {
        UserIdentityAvailability userIdentityAvailability = userService.checkEmailAvailability(email);

        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    @ApiOperation("Give admin role to user (only for admins)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}/giveAdmin")
    public ResponseEntity<ApiResponse> giveAdmin(@PathVariable String username) {
        ApiResponse apiResponse = userService.giveAdmin(username);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation("Take admin role from user (only for admins)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}/removeAdmin")
    public ResponseEntity<ApiResponse> removeAdmin(@PathVariable String username) {
        ApiResponse apiResponse = userService.removeAdmin(username);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
