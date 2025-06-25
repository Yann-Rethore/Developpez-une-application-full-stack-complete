// Classe de test d'intégration pour le contrôleur TopicController
package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Lance le contexte Spring Boot pour les tests d'intégration
@Transactional // Chaque test s'exécute dans une transaction isolée
class TopicControllerIT {

    @Autowired
    private TopicController topicController;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicService topicService;

    private User user;
    private Topic topic1;
    private Topic topic2;
    private Principal principal;

    @BeforeEach
    void setUp() {
        // Prépare un utilisateur et deux thèmes pour les tests
        user = new User();
        user.setUsername("integrationUser");
        user.setAbonnements(new HashSet<>());
        user.setEmail("test@test.com");
        user.setPassword("password");
        userRepository.save(user);

        topic1 = new Topic();
        topic1.setName("Java");
        topic1.setAbonnes(new HashSet<>());
        topic1.setDescription("Java");
        topicRepository.save(topic1);

        topic2 = new Topic();
        topic2.setName("Spring");
        topic2.setDescription("Spring Framework");
        topic2.setAbonnes(new HashSet<>());
        topicRepository.save(topic2);

        // Simule l'utilisateur authentifié
        principal = () -> "integrationUser";
    }

    @Test
    void getAllTopics_shouldReturnAllTopics() {
        // Vérifie que tous les thèmes sont bien retournés
        var topics = topicController.getAllTopics();
        assertThat(topics).hasSizeGreaterThanOrEqualTo(2);
        assertThat(topics.stream().anyMatch(t -> t.getName().equals("Java"))).isTrue();
    }

    @Test
    void getUserSubscriptions_shouldReturnEmptyInitially() {
        // Vérifie qu'aucun abonnement n'est présent initialement
        ResponseEntity<List<Long>> response = topicController.getUserSubscriptions(principal);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void subscribeToTopic_shouldAddSubscription() {
        // Vérifie que l'abonnement à un thème fonctionne
        ResponseEntity<?> response = topicController.subscribeToTopic(topic1.getId(), principal);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        User refreshed = userRepository.findWithAbonnementsByUsername("integrationUser").orElseThrow();
        assertThat(refreshed.getAbonnements()).extracting(Topic::getId).contains(topic1.getId());

        Topic refreshedTopic = topicRepository.findByIdWithAbonnes(topic1.getId()).orElseThrow();
        assertThat(refreshedTopic.getAbonnes()).extracting(User::getUsername).contains("integrationUser");
    }

    @Test
    void unsubscribeFromTopic_shouldRemoveSubscription() {
        // Abonne d'abord l'utilisateur, puis vérifie le désabonnement
        topicController.subscribeToTopic(topic2.getId(), principal);

        ResponseEntity<?> response = topicController.unsubscribeFromTopic(topic2.getId(), principal);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        User refreshed = userRepository.findWithAbonnementsByUsername("integrationUser").orElseThrow();
        assertThat(refreshed.getAbonnements()).extracting(Topic::getId).doesNotContain(topic2.getId());
    }

    @Test
    void getUserSubscriptions_shouldReturn401_whenNoPrincipal() {
        // Vérifie que l'accès sans authentification retourne 401
        ResponseEntity<List<Long>> response = topicController.getUserSubscriptions(null);
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }
}