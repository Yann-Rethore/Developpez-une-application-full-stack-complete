// Classe de test unitaire pour JwtUtil
package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Initialise JwtUtil et injecte les champs privés avant chaque test
    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        setField(jwtUtil, "jwtSecret", "zEk64YO/Ulef3ZkyCx2vrrKP833e3Vr4HGdeozU50jqRcfgXI4W9+5zaqlcFprM9F/vC4Rj4W8MiwwEoTcO/wA==");
        setField(jwtUtil, "jwtExpirationMs", 3600000L); // 1h
    }

    // Méthode utilitaire pour injecter une valeur dans un champ privé via la réflexion
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JwtUtil.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // Vérifie que la génération et la validation d'un token fonctionnent
    @Test
    void generateToken_and_validateJwtToken_shouldWork() {
        String username = "unitUser";
        String token = jwtUtil.generateToken(username);

        assertThat(token).isNotBlank();
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
    }

    // Vérifie que l'extraction du nom d'utilisateur depuis le token fonctionne
    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String username = "unitUser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        assertThat(extractedUsername).isEqualTo(username);
    }

    // Vérifie que la validation échoue avec un token invalide
    @Test
    void validateJwtToken_withInvalidToken_shouldReturnFalse() {
        String invalidToken = "invalid.token.value";
        assertThat(jwtUtil.validateJwtToken(invalidToken)).isFalse();
    }
}