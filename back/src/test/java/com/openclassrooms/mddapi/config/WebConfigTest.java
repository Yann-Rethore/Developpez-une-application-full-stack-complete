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
        WebConfig config = new WebConfig();
        WebMvcConfigurer configurer = config.corsConfigurer();
        assertThat(configurer).isNotNull();
    }

    @Test
    void corsConfigurer_shouldConfigureCorsMappings() {
        WebConfig config = new WebConfig();
        WebMvcConfigurer configurer = config.corsConfigurer();
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        when(registry.addMapping("/api/**")).thenReturn(registration);
        when(registration.allowedOrigins("http://localhost:4200")).thenReturn(registration);
        when(registration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(registration);
        when(registration.allowedHeaders("*")).thenReturn(registration);

        configurer.addCorsMappings(registry);

        verify(registry).addMapping("/api/**");
        verify(registration).allowedOrigins("http://localhost:4200");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(registration).allowedHeaders("*");
    }
}