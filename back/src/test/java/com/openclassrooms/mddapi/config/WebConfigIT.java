// Classe de test d'intégration pour la configuration CORS de l'application
package com.openclassrooms.mddapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Lance le contexte Spring Boot pour les tests d'intégration
class WebConfigIT {

    @Autowired
    @Qualifier("corsConfigurer") // Injection du bean nommé corsConfigurer
    private WebMvcConfigurer webMvcConfigurer;

    @Test
    void corsConfigurerBean_shouldBePresent() {
        // Vérifie que le bean WebMvcConfigurer pour le CORS est bien présent dans le contexte
        assertThat(webMvcConfigurer).isNotNull();
    }
}