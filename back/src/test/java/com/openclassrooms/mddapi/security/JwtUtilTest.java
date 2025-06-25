package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        setField(jwtUtil, "jwtSecret", "zEk64YO/Ulef3ZkyCx2vrrKP833e3Vr4HGdeozU50jqRcfgXI4W9+5zaqlcFprM9F/vC4Rj4W8MiwwEoTcO/wA==");
        setField(jwtUtil, "jwtExpirationMs", 3600000L); // 1h
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JwtUtil.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void generateToken_and_validateJwtToken_shouldWork() {
        String username = "unitUser";
        String token = jwtUtil.generateToken(username);

        assertThat(token).isNotBlank();
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String username = "unitUser";
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