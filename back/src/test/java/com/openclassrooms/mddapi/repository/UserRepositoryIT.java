// Classe de test d'intégration pour le repository UserRepository
package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Configure un contexte JPA en mémoire pour les tests
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository; // Repository testé

    @Autowired
    private TopicRepository topicRepository;

    @Test
    @DisplayName("findByEmail doit retourner l'utilisateur par email")
    void findByEmail_shouldReturnUser() {
        // Sauvegarde un utilisateur
        User user = userRepository.save(new User(null, "test@mail.com", "pass", "user1", null));
        // Recherche par email
        Optional<User> found = userRepository.findByEmail("test@mail.com");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("findByUsername doit retourner l'utilisateur par username")
    void findByUsername_shouldReturnUser() {
        // Sauvegarde un utilisateur
        User user = userRepository.save(new User(null, "a@a.com", "pass", "user2", null));
        // Recherche par username
        Optional<User> found = userRepository.findByUsername("user2");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("findWithAbonnementsByUsername doit charger les abonnements")
    void findWithAbonnementsByUsername_shouldFetchAbonnements() {
        // Prépare deux topics et un utilisateur abonné à ces topics
        Topic t1 = topicRepository.save(new Topic(null, "T1", "D1", null, null));
        Topic t2 = topicRepository.save(new Topic(null, "T2", "D2", null, null));
        User user = new User(null, "b@b.com", "pass", "user3", null);
        user.setAbonnements(new HashSet<>(List.of(t1, t2)));
        user = userRepository.save(user);

        // Recherche l'utilisateur avec ses abonnements
        Optional<User> found = userRepository.findWithAbonnementsByUsername("user3");
        assertThat(found).isPresent();
        assertThat(found.get().getAbonnements())
                .extracting(Topic::getId)
                .containsExactlyInAnyOrder(t1.getId(), t2.getId());
    }

    @Test
    @DisplayName("findWithAbonnementsAndArticlesByUsername doit charger abonnements et articles")
    void findWithAbonnementsAndArticlesByUsername_shouldFetchAbonnementsAndArticles() {
        // Prépare deux topics et un utilisateur abonné à ces topics
        Topic t1 = topicRepository.save(new Topic(null, "T1", "D1", null, null));
        Topic t2 = topicRepository.save(new Topic(null, "T2", "D2", null, null));
        User user = new User(null, "c@c.com", "pass", "user4", null);
        user.setAbonnements(new HashSet<>(List.of(t1, t2)));
        user = userRepository.save(user);

        // Recherche l'utilisateur avec ses abonnements et les articles de ces topics
        Optional<User> found = userRepository.findWithAbonnementsAndArticlesByUsername("user4");
        assertThat(found).isPresent();
        assertThat(found.get().getAbonnements())
                .extracting(Topic::getId)
                .containsExactlyInAnyOrder(t1.getId(), t2.getId());
    }
}