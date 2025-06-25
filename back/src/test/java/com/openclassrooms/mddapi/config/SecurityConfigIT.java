// Classe de test d'intégration pour la configuration de sécurité
package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.security.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Lance le contexte Spring Boot pour les tests d'intégration
class SecurityConfigIT {

    @Autowired
    private ApplicationContext context; // Permet d'accéder aux beans du contexte Spring

    @Test
    void passwordEncoderBean_shouldBePresentAndWork() {
        // Vérifie que le bean PasswordEncoder est présent et fonctionne
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        String encoded = encoder.encode("test");
        assertThat(encoder.matches("test", encoded)).isTrue(); // Vérifie que le mot de passe est bien encodé
    }

    @Test
    void securityFilterChainBean_shouldBePresent() {
        // Vérifie que le bean SecurityFilterChain est présent
        SecurityFilterChain chain = context.getBean(SecurityFilterChain.class);
        assertThat(chain).isNotNull();
    }

    @Test
    void jwtAuthFilterBean_shouldBePresent() {
        // Vérifie que le bean JwtAuthFilter est présent
        JwtAuthFilter filter = context.getBean(JwtAuthFilter.class);
        assertThat(filter).isNotNull();
    }
}