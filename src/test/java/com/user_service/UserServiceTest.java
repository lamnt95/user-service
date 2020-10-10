package com.user_service;

import com.user_service.exception.BadRequestException;
import com.user_service.exception.ResourceNotFoundException;
import com.user_service.model.user.User;
import com.user_service.payload.UserProfile;
import com.user_service.payload.request.UserRequest;
import com.user_service.repository.UserRepository;
import com.user_service.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetUserProfile_byUsername_shouldSuccess() {
        String username = "username";
        User user = new User();
        user.setEmail("user_email");
        user.setFirstName("user_firstname");
        user.setLastName("user_lastName");
        user.setPhone("0123456789");
        user.setWebsite("user_website.com");
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findByUsername(username)).thenReturn(userOptional);
        UserProfile userProfile = userServiceImpl.getUserProfile(username);

        assertEquals(user.getEmail(), userProfile.getEmail());
        assertEquals(user.getFirstName(), userProfile.getFirstName());
        assertEquals(user.getLastName(), userProfile.getLastName());
        assertEquals(user.getPhone(), userProfile.getPhone());
        assertEquals(user.getWebsite(), userProfile.getWebsite());

    }

    @Test
    public void testAddNewUser_existUsername_shouldFail() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username_request");

        when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(true);
        try {
            userServiceImpl.addNewUser(userRequest);
        } catch (Exception e) {
            assertTrue(e instanceof BadRequestException);
        }
    }

    @Test
    public void testAddNewUser_existEmail_shouldFail() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("email_request");
        userRequest.setUsername("username_request");

        when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);
        try {
            userServiceImpl.addNewUser(userRequest);
        } catch (Exception e) {
            assertTrue(e instanceof BadRequestException);
        }
    }
}
