// Classe de test d'intégration pour le repository CommentaireRepository
package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Commentaire;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Configure un contexte JPA en mémoire pour les tests
class CommentaireRepositoryIT {

    @Autowired
    private CommentaireRepository commentaireRepository; // Repository testé

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    void shouldSaveAndFindCommentaire() {
        // Prépare les entités nécessaires (topic, user, article)
        Topic topic = topicRepository.save(new Topic(null, "T", "D", null, null));
        User user = userRepository.save(new User(null, "mail@mail.com", "pass", "user", null));
        Article article = articleRepository.save(new Article(null, "Titre", "Contenu", topic, user, LocalDateTime.now(), null));
        Commentaire commentaire = new Commentaire(null, "Contenu commentaire", user, article, LocalDateTime.now());

        // Sauvegarde le commentaire en base
        Commentaire saved = commentaireRepository.save(commentaire);

        // Recherche le commentaire par son id
        Optional<Commentaire> found = commentaireRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getContenu()).isEqualTo("Contenu commentaire");
        assertThat(found.get().getArticle()).isEqualTo(article);
        assertThat(found.get().getCreateur()).isEqualTo(user);
    }

    @Test
    void shouldDeleteCommentaire() {
        // Prépare les entités nécessaires
        Topic topic = topicRepository.save(new Topic(null, "T2", "D2", null, null));
        User user = userRepository.save(new User(null, "mail2@mail.com", "pass", "user2", null));
        Article article = articleRepository.save(new Article(null, "Titre2", "Contenu2", topic, user, LocalDateTime.now(), null));
        Commentaire commentaire = commentaireRepository.save(new Commentaire(null, "À supprimer", user, article, LocalDateTime.now()));

        // Supprime le commentaire par son id
        commentaireRepository.deleteById(commentaire.getId());

        // Vérifie que le commentaire n'existe plus en base
        assertThat(commentaireRepository.findById(commentaire.getId())).isNotPresent();
    }
}