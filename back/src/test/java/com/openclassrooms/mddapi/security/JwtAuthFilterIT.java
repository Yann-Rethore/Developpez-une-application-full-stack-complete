package com.openclassrooms.mddapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest // Lance le contexte Spring complet pour le test
@ActiveProfiles("test") // Utilise le profil de test
class JwtAuthFilterIT {

    @Autowired
    private JwtAuthFilter jwtAuthFilter; // Filtre à tester

    @MockBean
    private JwtUtil jwtUtil; // Mock du composant utilitaire JWT

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        // Réinitialise le contexte de sécurité avant chaque test
        SecurityContextHolder.clearContext();
        // Mock des objets nécessaires à la requête
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void doFilterInternal_validToken_setsAuthentication() throws ServletException, IOException {
        // Simule un header Authorization avec un token valide
        String token = "valid.jwt.token";
        String username = "user";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateJwtToken(token)).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        // Exécute le filtre
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Vérifie que l'utilisateur est authentifié dans le contexte de sécurité
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(username);
        // Vérifie que la chaîne de filtres continue
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_doesNotSetAuthentication() throws ServletException, IOException {
        // Simule un header Authorization avec un token invalide
        String token = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateJwtToken(token)).thenReturn(false);

        // Exécute le filtre
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Vérifie qu'aucune authentification n'est définie
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_noAuthorizationHeader_doesNotSetAuthentication() throws ServletException, IOException {
        // Simule l'absence de header Authorization
        when(request.getHeader("Authorization")).thenReturn(null);

        // Exécute le filtre
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Vérifie qu'aucune authentification n'est définie
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}