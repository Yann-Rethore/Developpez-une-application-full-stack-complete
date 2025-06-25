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

@DataJpaTest
@Import({UserService.class, BCryptPasswordEncoder.class})
@ActiveProfiles("test")
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void register_shouldPersistUserWithEncodedPassword() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("testuser");
        request.setEmail("test@mail.com");
        request.setPassword("secret");

        User saved = userService.register(request);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("testuser");
        assertThat(saved.getEmail()).isEqualTo("test@mail.com");
        assertThat(passwordEncoder.matches("secret", saved.getPassword())).isTrue();
        assertThat(userRepository.findByUsername("testuser")).isPresent();
    }

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

    @Test
    void authenticate_shouldThrowIfUserNotFound() {
        assertThatThrownBy(() -> userService.authenticate("unknown", "nopass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Identifiants invalides");
    }
}