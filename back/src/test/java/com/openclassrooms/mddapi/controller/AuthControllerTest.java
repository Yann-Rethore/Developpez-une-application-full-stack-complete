// Classe de test unitaire pour le contrôleur AuthController
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
    private UserService userService; // Mock du service utilisateur

    @Mock
    private JwtUtil jwtUtil; // Mock de l'utilitaire JWT

    @InjectMocks
    private AuthController authController; // Contrôleur testé avec injection des mocks

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks avant chaque test
    }

    @Test
    void register_shouldReturnUser() {
        // Vérifie que l'inscription retourne bien l'utilisateur créé
        RegisterRequestDto request = new RegisterRequestDto();
        User user = new User();
        when(userService.register(request)).thenReturn(user);

        User result = authController.register(request);

        assertThat(result).isEqualTo(user);
        verify(userService).register(request);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        // Vérifie que la connexion retourne un token JWT si les identifiants sont valides
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
        // Vérifie que la connexion retourne une erreur 401 si le token JWT est null
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