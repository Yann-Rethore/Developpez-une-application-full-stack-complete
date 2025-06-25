package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.RegisterRequestDto;
import com.openclassrooms.mddapi.dto.LoginRequestDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldReturnUser() {
        RegisterRequestDto request = new RegisterRequestDto();
        User user = new User();
        when(userService.register(request)).thenReturn(user);

        User result = authController.register(request);

        assertThat(result).isEqualTo(user);
        verify(userService).register(request);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequestDto request = new LoginRequestDto();
        request.setIdentifier("user");
        request.setPassword("pass");
        User user = new User();
        user.setUsername("user");
        when(userService.authenticate("user", "pass")).thenReturn(user);
        when(jwtUtil.generateToken("user")).thenReturn("jwt-token");

        ResponseEntity<Map<String, String>> response = authController.login(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsEntry("token", "jwt-token");
    }

    @Test
    void login_shouldReturn401_whenTokenIsNull() {
        LoginRequestDto request = new LoginRequestDto();
        request.setIdentifier("user");
        request.setPassword("wrong");
        User user = new User();
        user.setUsername("user");
        when(userService.authenticate("user", "wrong")).thenReturn(user);
        when(jwtUtil.generateToken("user")).thenReturn(null);

        ResponseEntity<Map<String, String>> response = authController.login(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(response.getBody()).containsKey("error");
    }
}