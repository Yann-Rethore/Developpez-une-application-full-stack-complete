package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TopicRepositoryIT {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByIdWithAbonnes doit charger les abonnés du topic")
    void findByIdWithAbonnes_shouldFetchAbonnes() {
        Topic topic = topicRepository.save(new Topic(null, "T1", "D1", null, null));
        User user1 = userRepository.save(new User(null, "a@a.com", "pass", "user1", null));
        User user2 = userRepository.save(new User(null, "b@b.com", "pass", "user2", null));
        // Ajout des abonnés
        topic.setAbonnes(new HashSet<>(List.of(user1, user2)));
        topic = topicRepository.save(topic);

        Optional<Topic> opt = topicRepository.findByIdWithAbonnes(topic.getId());

        assertThat(opt).isPresent();
        Topic fetched = opt.get();
        assertThat(fetched.getAbonnes())
                .extracting(User::getId)
                .containsExactlyInAnyOrder(user1.getId(), user2.getId());
    }

    @Test
    @DisplayName("findAbonnementsByUsername doit retourner les topics suivis par l'utilisateur")
    void findAbonnementsByUsername_shouldReturnTopics() {
        Topic topic1 = topicRepository.save(new Topic(null, "T1", "D1", null, null));
        Topic topic2 = topicRepository.save(new Topic(null, "T2", "D2", null, null));
        User user = userRepository.save(new User(null, "c@c.com", "pass", "user3", null));
        // Ajout des abonnements
        user.setAbonnements(new HashSet<>(List.of(topic1, topic2)));
        userRepository.save(user);

        List<Topic> abonnements = topicRepository.findAbonnementsByUsername("user3");

        assertThat(abonnements)
                .extracting(Topic::getId)
                .containsExactlyInAnyOrder(topic1.getId(), topic2.getId());
    }
}