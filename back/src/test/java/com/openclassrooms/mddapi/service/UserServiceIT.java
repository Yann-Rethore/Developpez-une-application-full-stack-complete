// Classe de test d'intégration pour UserService
package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.RegisterRequestDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest // Configure un contexte JPA en mémoire pour les tests
@Import({UserService.class, BCryptPasswordEncoder.class}) // Importe le service et l'encodeur de mot de passe
@ActiveProfiles("test") // Utilise le profil de test
class UserServiceIT {

    @Autowired
    private UserService userService; // Service à tester

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Teste l'inscription d'un utilisateur et vérifie l'encodage du mot de passe
    @Test
    void register_shouldPersistUserWithEncodedPassword() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("testuser");
        request.setEmail("test@mail.com");
        request.setPassword("secret");

        User saved = userService.register(request);

        assertThat(saved.getId()).isNotNull(); // L'utilisateur a bien un id généré
        assertThat(saved.getUsername()).isEqualTo("testuser");
        assertThat(saved.getEmail()).isEqualTo("test@mail.com");
        assertThat(passwordEncoder.matches("secret", saved.getPassword())).isTrue(); // Le mot de passe est encodé
        assertThat(userRepository.findByUsername("testuser")).isPresent(); // L'utilisateur est bien en base
    }

    // Teste l'authentification par nom d'utilisateur
    @Test
    void authenticate_shouldReturnUserByUsername() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("authuser");
        request.setEmail("auth@mail.com");
        request.setPassword("mypassword");
        userService.register(request);

        User authenticated = userService.authenticate("authuser", "mypassword");

        assertThat(authenticated).isNotNull();
        assertThat(authenticated.getUsername()).isEqualTo("authuser");
    }

    // Teste l'authentification par email
    @Test
    void authenticate_shouldReturnUserByEmail() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("mailuser");
        request.setEmail("mail@mail.com");
        request.setPassword("mailpass");
        userService.register(request);

        User authenticated = userService.authenticate("mail@mail.com", "mailpass");

        assertThat(authenticated).isNotNull();
        assertThat(authenticated.getEmail()).isEqualTo("mail@mail.com");
    }

    // Vérifie qu'une exception est levée si le mot de passe est incorrect
    @Test
    void authenticate_shouldThrowIfPasswordIsWrong() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("failuser");
        request.setEmail("fail@mail.com");
        request.setPassword("goodpass");
        userService.register(request);

        assertThatThrownBy(() -> userService.authenticate("failuser", "badpass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Identifiants invalides");
    }

    // Vérifie qu'une exception est levée si l'utilisateur n'existe pas
    @Test
    void authenticate_shouldThrowIfUserNotFound() {
        assertThatThrownBy(() -> userService.authenticate("unknown", "nopass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Identifiants invalides");
    }
}