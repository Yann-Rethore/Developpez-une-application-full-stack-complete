// Classe de test unitaire pour UserService
package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.RegisterRequestDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mock du repository utilisateur

    @Mock
    private PasswordEncoder passwordEncoder; // Mock de l'encodeur de mot de passe

    @InjectMocks
    private UserService userService; // Service à tester

    // Initialise les mocks avant chaque test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Vérifie que register encode le mot de passe et sauvegarde l'utilisateur
    @Test
    void register_shouldEncodePasswordAndSaveUser() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("user");
        request.setEmail("user@mail.com");
        request.setPassword("plain");

        User savedUser = new User();
        savedUser.setUsername("user");
        savedUser.setEmail("user@mail.com");
        savedUser.setPassword("encoded");

        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(request);

        assertThat(result.getUsername()).isEqualTo("user");
        assertThat(result.getEmail()).isEqualTo("user@mail.com");
        assertThat(result.getPassword()).isEqualTo("encoded");
        verify(passwordEncoder).encode("plain");
        verify(userRepository).save(any(User.class));
    }

    // Vérifie que authenticate retourne l'utilisateur par username si le mot de passe correspond
    @Test
    void authenticate_shouldReturnUserByUsernameIfPasswordMatches() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("encoded");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain", "encoded")).thenReturn(true);

        User result = userService.authenticate("user", "plain");

        assertThat(result).isEqualTo(user);
        verify(userRepository).findByUsername("user");
        verify(passwordEncoder).matches("plain", "encoded");
    }

    // Vérifie que authenticate retourne l'utilisateur par email si le mot de passe correspond
    @Test
    void authenticate_shouldReturnUserByEmailIfPasswordMatches() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByUsername("user@mail.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain", "encoded")).thenReturn(true);

        User result = userService.authenticate("user@mail.com", "plain");

        assertThat(result).isEqualTo(user);
        verify(userRepository).findByUsername("user@mail.com");
        verify(userRepository).findByEmail("user@mail.com");
        verify(passwordEncoder).matches("plain", "encoded");
    }

    // Vérifie qu'une exception est levée si l'utilisateur n'est pas trouvé
    @Test
    void authenticate_shouldThrowIfUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.authenticate("unknown", "pass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Identifiants invalides");
    }

    // Vérifie qu'une exception est levée si le mot de passe ne correspond pas
    @Test
    void authenticate_shouldThrowIfPasswordDoesNotMatch() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("encoded");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThatThrownBy(() -> userService.authenticate("user", "wrong"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Identifiants invalides");
    }
}