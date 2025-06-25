// Classe de test d'intégration pour le contrôleur AuthController
package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.RegisterRequestDto;
import com.openclassrooms.mddapi.dto.LoginRequestDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Lance le contexte Spring Boot pour les tests d'intégration
@Transactional // Garantit que chaque test s'exécute dans une transaction isolée
class AuthControllerIT {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void register_shouldPersistUser() {
        // Vérifie que l'inscription d'un utilisateur persiste bien en base
        RegisterRequestDto request = new RegisterRequestDto();
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
        // Vérifie que la connexion retourne un token JWT valide avec de bons identifiants
        RegisterRequestDto reg = new RegisterRequestDto();
        reg.setUsername("loginUser");
        reg.setEmail("login@test.com");
        reg.setPassword("password");
        authController.register(reg);

        LoginRequestDto request = new LoginRequestDto();
        request.setIdentifier("loginUser");
        request.setPassword("password");

        ResponseEntity<Map<String, String>> response = authController.login(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsKey("token");
        String token = response.getBody().get("token");
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
    }

    @Test
    void login_shouldReturn401_whenCredentialsAreInvalid() {
        // Vérifie que la connexion échoue avec de mauvais identifiants
        LoginRequestDto request = new LoginRequestDto();
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