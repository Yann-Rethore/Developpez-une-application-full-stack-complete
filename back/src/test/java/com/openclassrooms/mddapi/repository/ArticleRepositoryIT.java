// Classe de test d'intégration pour ArticleRepository
package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Commentaire;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Configure un contexte Spring JPA en mémoire pour les tests
class ArticleRepositoryIT {

    @Autowired
    private ArticleRepository articleRepository; // Repository testé

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Test
    @DisplayName("findByTopicIdIn doit retourner les articles des topics donnés")
    void findByTopicIdIn_shouldReturnArticles() {
        // Prépare deux topics et un utilisateur
        Topic topic1 = topicRepository.save(new Topic(null, "T1", "D1", null, null));
        Topic topic2 = topicRepository.save(new Topic(null, "T2", "D2", null, null));
        User user = userRepository.save(new User(null, "a@a.com", "pass", "user", null));
        // Crée deux articles associés aux topics
        Article a1 = articleRepository.save(new Article(null, "Titre1", "Contenu1", topic1, user, LocalDateTime.now(), null));
        Article a2 = articleRepository.save(new Article(null, "Titre2", "Contenu2", topic2, user, LocalDateTime.now(), null));

        // Teste la récupération par IDs de topics
        List<Article> articles = articleRepository.findByTopicIdIn(Set.of(topic1.getId(), topic2.getId()));

        assertThat(articles).extracting(Article::getId).containsExactlyInAnyOrder(a1.getId(), a2.getId());
    }

    @Test
    @DisplayName("findByTopicsWithCommentaires doit charger les commentaires")
    void findByTopicsWithCommentaires_shouldFetchCommentaires() {
        // Prépare un topic, un utilisateur, un article et un commentaire
        Topic topic = topicRepository.save(new Topic(null, "T", "D", null, null));
        User user = userRepository.save(new User(null, "b@b.com", "pass", "user2", null));
        Article article = articleRepository.save(new Article(null, "Titre", "Contenu", topic, user, LocalDateTime.now(), null));
        Commentaire commentaire = commentaireRepository.save(new Commentaire(null, "C", user, article, LocalDateTime.now()));

        // Teste la récupération des articles avec leurs commentaires
        List<Article> articles = articleRepository.findByTopicsWithCommentaires(List.of(topic));

        assertThat(articles).hasSize(1);
    }

    @Test
    @DisplayName("findByIdWithCommentaires doit retourner l'article avec ses commentaires")
    void findByIdWithCommentaires_shouldReturnArticleWithCommentaires() {
        // Prépare un topic, un utilisateur, un article et un commentaire
        Topic topic = topicRepository.save(new Topic(null, "T", "D", null, null));
        User user = userRepository.save(new User(null, "c@c.com", "pass", "user3", null));
        Article article = articleRepository.save(new Article(null, "Titre", "Contenu", topic, user, LocalDateTime.now(), null));
        Commentaire commentaire = new Commentaire(null, "C", user, article, LocalDateTime.now());
        Commentaire saved = commentaireRepository.save(commentaire);

        // Teste la récupération d'un article par son id avec ses commentaires
        Optional<Article> articleWithCommentaires = articleRepository.findByIdWithCommentaires(article.getId());

        assertThat(articleWithCommentaires).isPresent();
        Article articleFetched = articleWithCommentaires.get();

        assertThat(articleFetched.getId()).isEqualTo(article.getId());
    }
}