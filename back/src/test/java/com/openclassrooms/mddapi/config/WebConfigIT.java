package com.openclassrooms.mddapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebConfigIT {

    @Autowired
    @Qualifier("corsConfigurer")
    private WebMvcConfigurer webMvcConfigurer;

    @Test
    void corsConfigurerBean_shouldBePresent() {
        assertThat(webMvcConfigurer).isNotNull();
    }
}