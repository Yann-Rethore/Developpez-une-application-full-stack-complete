package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.security.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigIT {

    @Autowired
    private ApplicationContext context;

    @Test
    void passwordEncoderBean_shouldBePresentAndWork() {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        String encoded = encoder.encode("test");
        assertThat(encoder.matches("test", encoded)).isTrue();
    }

    @Test
    void securityFilterChainBean_shouldBePresent() {
        SecurityFilterChain chain = context.getBean(SecurityFilterChain.class);
        assertThat(chain).isNotNull();
    }

    @Test
    void jwtAuthFilterBean_shouldBePresent() {
        JwtAuthFilter filter = context.getBean(JwtAuthFilter.class);
        assertThat(filter).isNotNull();
    }
}