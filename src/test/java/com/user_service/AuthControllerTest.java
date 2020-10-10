package com.user_service;

import com.user_service.controller.AuthController;
import com.user_service.payload.request.RegisterRequest;
import com.user_service.repository.RoleRepository;
import com.user_service.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testRegister_withUserExistEmail_shouldRegisterFail() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("user1@gmail.com")
                .firstName("user1firstname")
                .lastName("user1lastname")
                .username("user1username")
                .password("user1password")
                .build();
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);
        try {
            authController.registerUser(registerRequest);
        } catch (Exception e) {
            assertEquals("Email is already taken.", e.getMessage());
        }
    }

    @Test
    public void testRegister_withUserExistUsername_shouldRegisterFail() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("user1@gmail.com")
                .firstName("user1firstname")
                .lastName("user1lastname")
                .username("user1username")
                .password("user1password")
                .build();
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);
        try {
            authController.registerUser(registerRequest);
        } catch (Exception e) {
            assertEquals("Username is already taken.", e.getMessage());
        }
    }

}
