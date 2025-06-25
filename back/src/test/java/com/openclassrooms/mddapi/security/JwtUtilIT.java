// src/test/java/com/openclassrooms/mddapi/security/JwtUtilIT.java
package com.openclassrooms.mddapi.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JwtUtilIT {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generateToken_and_validateJwtToken_shouldWork() {
        String username = "integrationUser";
        String token = jwtUtil.generateToken(username);

        assertThat(token).isNotBlank();
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String username = "integrationUser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    void validateJwtToken_withInvalidToken_shouldReturnFalse() {
        String invalidToken = "invalid.token.value";
        assertThat(jwtUtil.validateJwtToken(invalidToken)).isFalse();
    }
}