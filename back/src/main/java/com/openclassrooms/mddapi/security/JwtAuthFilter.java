// Filtre d'authentification JWT pour chaque requête HTTP
package com.openclassrooms.mddapi.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component // Indique que ce filtre est un composant Spring
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Utilitaire pour gérer les opérations JWT

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Récupère le header Authorization de la requête
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Vérifie si le header commence par "Bearer " et extrait le token
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            // Valide le token JWT
            if (jwtUtil.validateJwtToken(token)) {
                // Extrait le nom d'utilisateur du token
                username = jwtUtil.getUsernameFromToken(token);
                logger.info("Authenticated user: {}", username);
                // Crée un objet d'authentification pour Spring Security
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, null);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Définit l'utilisateur authentifié dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        // Continue la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}