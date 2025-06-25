// Classe de configuration de la sécurité Spring Security
package com.openclassrooms.mddapi.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.openclassrooms.mddapi.security.JwtAuthFilter;

@Configuration // Indique que cette classe contient des beans de configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter; // Filtre d'authentification JWT personnalisé

    // Définition de la chaîne de filtres de sécurité
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF (utile pour les API REST)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/api/auth/**").permitAll() // Autorise l'accès sans authentification à ces endpoints
                        .requestMatchers("/api/themes/**").authenticated() // Nécessite une authentification pour ces endpoints
                        .requestMatchers("/api/profile/**").authenticated()
                        .requestMatchers("/api/article/**").authenticated()
                        .anyRequest().authenticated() // Toute autre requête nécessite une authentification
                )
                .exceptionHandling(eh -> eh
                        // Gère les erreurs d'authentification en renvoyant une erreur 401
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                );

        // Ajoute le filtre JWT avant le filtre d'authentification standard
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Bean pour encoder les mots de passe avec BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}