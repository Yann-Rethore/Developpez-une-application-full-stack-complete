package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserProfileDto;
import com.openclassrooms.mddapi.dto.UserProfileUpdateDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserControllerIT {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private Topic topic1;
    private Topic topic2;
    private Principal principal;

    @BeforeEach
    void setUp() {
        topic1 = new Topic();
        topic1.setName("Java");
        topic1.setDescription("desc1");
        topicRepository.save(topic1);

        topic2 = new Topic();
        topic2.setName("Spring");
        topic2.setDescription("desc2");
        topicRepository.save(topic2);

        user = new User();
        user.setUsername("integrationUser");
        user.setEmail("integration@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAbonnements(new HashSet<>(List.of(topic1, topic2)));
        userRepository.save(user);

        principal = () -> "integrationUser";
    }

    @Test
    void getProfile_shouldReturnUserProfileDTO() {
        ResponseEntity<UserProfileDto> response = userController.getProfile(principal);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        UserProfileDto dto = response.getBody();
        assertThat(dto.getUsername()).isEqualTo("integrationUser");
        assertThat(dto.getEmail()).isEqualTo("integration@test.com");
        assertThat(dto.getAbonnements()).extracting("id").contains(topic1.getId(), topic2.getId());
    }

    @Test
    void updateProfile_shouldUpdateFieldsAndDesabonnements() {
        UserProfileUpdateDto updates = new UserProfileUpdateDto();
        updates.setUsername("newuser");
        updates.setEmail("new@test.com");
        updates.setPassword("newpass");
        updates.setDesabonnements(List.of(topic1.getId()));

        ResponseEntity<?> response = userController.updateProfile(principal, updates);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        User refreshed = userRepository.findWithAbonnementsByUsername("newuser").orElseThrow();
        assertThat(refreshed.getUsername()).isEqualTo("newuser");
        assertThat(refreshed.getEmail()).isEqualTo("new@test.com");
        assertThat(passwordEncoder.matches("newpass", refreshed.getPassword())).isTrue();
        assertThat(refreshed.getAbonnements()).extracting(Topic::getId).containsExactly(topic2.getId());
    }
}