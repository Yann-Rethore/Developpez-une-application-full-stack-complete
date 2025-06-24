package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleCreateDTO;
import com.openclassrooms.mddapi.dto.CommentaireRequestDto;
import com.openclassrooms.mddapi.model.*;
import com.openclassrooms.mddapi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("intégration")
@SpringBootTest
class ArticleControllerIT {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CommentaireRepository commentaireRepository;
    @Autowired
    private ArticleController articleController;

    private User user;
    private Topic topic;

    private Principal principalMock(String username) {
        return () -> username;
    }

    @BeforeEach
    void setUp() {
        commentaireRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
        topicRepository.deleteAll();

        user = new User();
        user.setUsername("user");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        topic = new Topic();
        topic.setName("Tech");
        topic.setDescription("Technologie et innovation");
        topic = topicRepository.save(topic);

        user.setAbonnements(Set.of(topic));
        userRepository.save(user);
    }

    @Test
    void createArticle_shouldPersistArticle() {
        ArticleCreateDTO dto = new ArticleCreateDTO();
        dto.setTitre("Titre");
        dto.setContenu("Contenu");
        dto.setThemeId(topic.getId());

        ResponseEntity<?> response = articleController.createArticle(dto, principalMock("user"));
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(articleRepository.findAll()).hasSize(1);
    }

    @Test
    void getArticlesAbonnes_shouldReturnArticles() {
        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user);
        article.setTopic(topic);
        articleRepository.save(article);

        ResponseEntity<?> response = articleController.getArticlesAbonnes(principalMock("user"));
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(((java.util.List<?>) response.getBody())).hasSize(1);
    }

    @Test
    void getArticleById_shouldReturnArticle() {
        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user);
        article.setTopic(topic);
        article = articleRepository.save(article);

        ResponseEntity<?> response = articleController.getArticleById(article.getId());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void ajouterCommentaire_shouldPersistComment() {
        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user);
        article.setTopic(topic);
        article = articleRepository.save(article);

        CommentaireRequestDto dto = new CommentaireRequestDto();
        dto.setContenu("Un commentaire");

        ResponseEntity<?> response = articleController.ajouterCommentaire(article.getId(), dto, principalMock("user"));
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(commentaireRepository.findAll()).hasSize(1);
    }
}