// Classe de test d'intégration pour JwtUtil
package com.openclassrooms.mddapi.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest // Lance le contexte Spring complet pour injecter les propriétés et beans
class JwtUtilIT {

    @Autowired
    private JwtUtil jwtUtil; // Utilitaire JWT à tester

    @Test
    void generateToken_and_validateJwtToken_shouldWork() {
        // Génère un token pour un utilisateur
        String username = "integrationUser";
        String token = jwtUtil.generateToken(username);

        // Vérifie que le token n'est pas vide et qu'il est valide
        assertThat(token).isNotBlank();
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        // Génère un token et extrait le nom d'utilisateur
        String username = "integrationUser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        // Vérifie que le nom d'utilisateur extrait correspond à l'original
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    void validateJwtToken_withInvalidToken_shouldReturnFalse() {
        // Vérifie qu'un token invalide est bien rejeté
        String invalidToken = "invalid.token.value";
        assertThat(jwtUtil.validateJwtToken(invalidToken)).isFalse();
    }
}