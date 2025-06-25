// Classe de test unitaire pour la configuration CORS de WebConfig
package com.openclassrooms.mddapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WebConfigTest {

    @Test
    void corsConfigurer_shouldReturnWebMvcConfigurerBean() {
        // Vérifie que le bean WebMvcConfigurer est bien créé par WebConfig
        WebConfig config = new WebConfig();
        WebMvcConfigurer configurer = config.corsConfigurer();
        assertThat(configurer).isNotNull();
    }

    @Test
    void corsConfigurer_shouldConfigureCorsMappings() {
        // Vérifie que la configuration CORS applique bien les règles attendues
        WebConfig config = new WebConfig();
        WebMvcConfigurer configurer = config.corsConfigurer();
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        // Prépare les mocks pour simuler la chaîne de configuration
        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedOrigins("http://localhost:4200")).thenReturn(registration);
        when(registration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(registration);
        when(registration.allowedHeaders("*")).thenReturn(registration);

        // Exécute la configuration CORS
        configurer.addCorsMappings(registry);

        // Vérifie que chaque méthode de configuration a bien été appelée avec les bons paramètres
        verify(registry).addMapping("/api/**");
        verify(registration).allowedOrigins("http://localhost:4200");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(registration).allowedHeaders("*");
    }
}