package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test") // Utilise le profil de test
@DataJpaTest // Configure un contexte JPA en mémoire pour les tests
@Import(ArticleService.class) // Importe le service à tester
class ArticleServiceIT {

    @Autowired
    private ArticleService articleService; // Service à tester

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    // Teste la sauvegarde et la récupération de tous les articles
    @Test
    void saveAndFindAll_shouldPersistAndReturnArticles() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Topic topic = new Topic();
        topic.setName("Test Topic");
        topic.setDescription("Test Description");
        topic = topicRepository.save(topic);

        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user); // L’auteur doit être sauvegardé
        article.setTopic(topic);
        articleRepository.save(article);

        List<Article> articles = articleService.findAll();
        assertThat(articles).hasSize(1);
        assertThat(articles.get(0).getTitre()).isEqualTo("Titre");
    }

    // Teste la récupération d’un article par son identifiant
    @Test
    void findById_shouldReturnSavedArticle() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Topic topic = new Topic();
        topic.setName("Test Topic");
        topic.setDescription("Test Description");
        topic = topicRepository.save(topic);

        Article article = new Article();
        article.setTitre("Titre2");
        article.setAuteur(user);
        article.setTopic(topic);
        article.setContenu("Contenu2");
        Article saved = articleRepository.save(article);

        Optional<Article> found = articleService.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitre()).isEqualTo("Titre2");
    }

    // Teste la suppression d’un article par son identifiant
    @Test
    void deleteById_shouldRemoveArticle() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Topic topic = new Topic();
        topic.setName("Test Topic");
        topic.setDescription("Test Description");
        topic = topicRepository.save(topic);

        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user);
        article.setTopic(topic);
        articleRepository.save(article);

        articleService.deleteById(article.getId());
        assertThat(articleRepository.findById(article.getId())).isEmpty();
    }

    // Teste la récupération des articles liés aux abonnements de l’utilisateur
    @Test
    void findArticlesByAbonnements_shouldReturnArticlesForUserTopics() {
        // Création et sauvegarde d’un utilisateur
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        // Création et sauvegarde d’un topic
        Topic topic = new Topic();
        topic.setName("Java");
        topic.setDescription("Langage de programmation");
        topic = topicRepository.save(topic);

        // L’utilisateur s’abonne au topic
        user.getAbonnements().add(topic);
        user = userRepository.save(user);

        // Création et sauvegarde d’un article lié au topic et à l’auteur
        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user);
        article.setTopic(topic);
        article = articleRepository.save(article);

        // Appel du service et vérification
        List<Article> articles = articleService.findArticlesByAbonnements(user);
        assertThat(articles).hasSize(1);
        assertThat(articles.get(0).getTitre()).isEqualTo("Titre");
    }
}