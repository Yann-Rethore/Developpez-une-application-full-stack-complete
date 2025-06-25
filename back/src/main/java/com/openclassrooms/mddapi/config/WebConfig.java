// Classe de configuration pour le CORS dans l'application Spring Boot
package com.openclassrooms.mddapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Indique que cette classe fournit des beans de configuration Spring
public class WebConfig {
    @Bean // Déclare un bean pour configurer le CORS
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            // Configure les règles CORS pour les endpoints de l'API
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Applique la configuration CORS à tous les endpoints /api/**
                        .allowedOrigins("http://localhost:4200") // Autorise uniquement ce domaine (ex : front Angular)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                        .allowedHeaders("*"); // Autorise tous les headers
            }
        };
    }
}