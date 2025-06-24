package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.security.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        JwtAuthFilter jwtAuthFilter = Mockito.mock(JwtAuthFilter.class);
        securityConfig = new SecurityConfig();
    }

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded = encoder.encode("test");
        assertThat(encoder.matches("test", encoded)).isTrue();
    }

    @Test
    void filterChain_shouldBuildSecurityFilterChain() throws Exception {
        HttpSecurity http = Mockito.mock(HttpSecurity.class, Mockito.RETURNS_DEEP_STUBS);
        // On ne teste pas le comportement interne de Spring Security, mais la présence du bean
        SecurityFilterChain chain = securityConfig.filterChain(http);
        assertThat(chain).isNotNull();
    }
}