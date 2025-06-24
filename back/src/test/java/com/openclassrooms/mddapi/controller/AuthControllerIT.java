package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtil;
import com.openclassrooms.mddapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthControllerIT {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void register_shouldPersistUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("integrationUser");
        request.setEmail("integration@test.com");
        request.setPassword("password");

        User user = authController.register(request);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("integrationUser");
        assertThat(user.getEmail()).isEqualTo("integration@test.com");
        assertThat(userRepository.findByUsername("integrationUser")).isPresent();
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        // Préparer un utilisateur
        RegisterRequest reg = new RegisterRequest();
        reg.setUsername("loginUser");
        reg.setEmail("login@test.com");
        reg.setPassword("password");
        authController.register(reg);

        LoginRequest request = new LoginRequest();
        request.setIdentifier("loginUser");
        request.setPassword("password");

        ResponseEntity<Map<String, String>> response = authController.login(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsKey("token");
        // Vérifie que le token est valide
        String token = response.getBody().get("token");
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
    }

    @Test
    void login_shouldReturn401_whenCredentialsAreInvalid() {
        LoginRequest request = new LoginRequest();
        request.setIdentifier("unknownUser");
        request.setPassword("wrong");

        try {
            authController.login(request);
            fail("Une exception aurait dû être levée");
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Identifiants invalides");
        }
    }
}