package com.openclassrooms.mddapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Clé de 32 caractères (256 bits)
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "zEk64YO/Ulef3ZkyCx2vrrKP833e3Vr4HGdeozU50jqRcfgXI4W9+5zaqlcFprM9F/vC4Rj4W8MiwwEoTcO/wA==");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 3600000L); // 1h
    }


    @Test
    void generateToken_and_validateJwtToken_shouldWork() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertThat(token).isNotBlank();
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String username = "testuser";
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