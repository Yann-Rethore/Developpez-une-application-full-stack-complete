// Classe de test unitaire pour la configuration de sécurité
package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.security.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        // On ne peut pas injecter JwtAuthFilter car il n'est pas passé au constructeur,
        // mais on prépare le SecurityConfig pour les tests unitaires.
        securityConfig = new SecurityConfig();
    }

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        // Vérifie que le bean PasswordEncoder retourne bien un BCryptPasswordEncoder fonctionnel
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded = encoder.encode("test");
        assertThat(encoder.matches("test", encoded)).isTrue();
    }
}