// Classe de test unitaire pour le contrôleur UserController
package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.dto.UserProfileDto;
import com.openclassrooms.mddapi.dto.UserProfileUpdateDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController; // Contrôleur testé avec injection des mocks

    @Mock
    private UserRepository userRepository; // Mock du repository utilisateur
    @Mock
    private TopicRepository topicRepository; // Mock du repository thème
    @Mock
    private PasswordEncoder passwordEncoder; // Mock de l'encodeur de mot de passe

    @Mock
    private Principal principal; // Mock du principal (utilisateur connecté)

    private User user;
    private Topic topic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks avant chaque test
        user = new User();
        user.setUsername("user");
        user.setEmail("user@test.com");
        topic = new Topic();
        topic.setId(1L);
        topic.setName("Java");
        topic.setDescription("desc");
        user.setAbonnements(new HashSet<>(Set.of(topic)));
    }

    @Test
    void getProfile_shouldReturnUserProfileDTO() {
        // Vérifie que la récupération du profil retourne bien les infos attendues
        when(principal.getName()).thenReturn("user");
        when(userRepository.findWithAbonnementsByUsername("user")).thenReturn(Optional.of(user));

        ResponseEntity<UserProfileDto> response = userController.getProfile(principal);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        UserProfileDto dto = response.getBody();
        assertThat(dto.getUsername()).isEqualTo("user");
        assertThat(dto.getEmail()).isEqualTo("user@test.com");
        assertThat(dto.getAbonnements()).extracting(TopicDto::getId).contains(1L);
    }

    @Test
    void updateProfile_shouldUpdateFields() {
        // Vérifie que la mise à jour du profil modifie bien les champs et gère les désabonnements
        when(principal.getName()).thenReturn("user");
        when(userRepository.findWithAbonnementsByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encoded");

        UserProfileUpdateDto updates = new UserProfileUpdateDto();
        updates.setUsername("newuser");
        updates.setEmail("new@test.com");
        updates.setPassword("newpass");
        updates.setDesabonnements(List.of(1L));

        ResponseEntity<?> response = userController.updateProfile(principal, updates);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(user.getUsername()).isEqualTo("newuser");
        assertThat(user.getEmail()).isEqualTo("new@test.com");
        assertThat(user.getPassword()).isEqualTo("encoded");
        assertThat(user.getAbonnements()).isEmpty();
        verify(userRepository).save(user);
    }

    @Test
    void updateProfile_shouldNotUpdateFieldsIfNull() {
        // Vérifie qu'aucune modification n'est faite si les champs sont à null
        when(principal.getName()).thenReturn("user");
        when(userRepository.findWithAbonnementsByUsername("user")).thenReturn(Optional.of(user));

        UserProfileUpdateDto updates = new UserProfileUpdateDto();
        updates.setUsername(null);
        updates.setEmail(null);
        updates.setPassword(null);
        updates.setDesabonnements(null);

        ResponseEntity<?> response = userController.updateProfile(principal, updates);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getEmail()).isEqualTo("user@test.com");
        assertThat(user.getPassword()).isNull();
        assertThat(user.getAbonnements()).hasSize(1);
        verify(userRepository).save(user);
    }

    @Test
    void updateProfile_shouldNotRemoveAbonnementsIfDesabonnementsEmpty() {
        // Vérifie qu'aucun abonnement n'est retiré si la liste des désabonnements est vide
        when(principal.getName()).thenReturn("user");
        when(userRepository.findWithAbonnementsByUsername("user")).thenReturn(Optional.of(user));

        UserProfileUpdateDto updates = new UserProfileUpdateDto();
        updates.setDesabonnements(Collections.emptyList());

        ResponseEntity<?> response = userController.updateProfile(principal, updates);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(user.getAbonnements()).hasSize(1);
        verify(userRepository).save(user);
    }
}