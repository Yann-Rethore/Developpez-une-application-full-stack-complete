// Classe de test unitaire pour JwtAuthFilter
package com.openclassrooms.mddapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil; // Mock de l'utilitaire JWT

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter; // Filtre à tester

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext(); // Réinitialise le contexte de sécurité
    }

    @Test
    void doFilterInternal_validToken_setsAuthentication() throws ServletException, IOException {
        // Simule un header Authorization avec un token valide
        String token = "valid.jwt.token";
        String username = "user";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateJwtToken(token)).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Vérifie que l'utilisateur est authentifié dans le contexte
        assert(SecurityContextHolder.getContext().getAuthentication() != null);
        assert(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(username));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_doesNotSetAuthentication() throws ServletException, IOException {
        // Simule un token invalide
        String token = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateJwtToken(token)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Vérifie qu'il n'y a pas d'authentification dans le contexte
        assert(SecurityContextHolder.getContext().getAuthentication() == null);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_noAuthorizationHeader_doesNotSetAuthentication() throws ServletException, IOException {
        // Simule l'absence de header Authorization
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assert(SecurityContextHolder.getContext().getAuthentication() == null);
        verify(filterChain).doFilter(request, response);
    }
}