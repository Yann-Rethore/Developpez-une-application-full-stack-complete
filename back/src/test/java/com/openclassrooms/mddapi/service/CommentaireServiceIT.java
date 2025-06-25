// Classe de test d'intégration pour CommentaireService
package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Commentaire;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Configure un contexte JPA en mémoire pour les tests
@Import({CommentaireService.class, TopicService.class}) // Importe les services nécessaires
@ActiveProfiles("test") // Utilise le profil de test
class CommentaireServiceIT {

    @Autowired
    private CommentaireService commentaireService; // Service à tester

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TopicService topicService;

    // Teste la sauvegarde et la récupération de tous les commentaires
    @Test
    void saveAndFindAll_shouldPersistAndReturnCommentaires() {
        // Création et sauvegarde d'un utilisateur
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        // Création et sauvegarde d'un topic
        Topic topic = new Topic();
        topic.setName("Java");
        topic.setDescription("Langage de programmation");
        topicService.save(topic);

        // Création et sauvegarde d’un article
        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user);
        article.setTopic(topic);
        article = articleRepository.save(article);

        // Création et sauvegarde du commentaire
        Commentaire commentaire = new Commentaire();
        commentaire.setContenu("Test contenu");
        commentaire.setCreateur(user);
        commentaire.setArticle(article);
        Commentaire saved = commentaireService.save(commentaire);

        // Vérifie que le commentaire est bien sauvegardé et récupéré
        List<Commentaire> commentaires = commentaireService.findAll();
        assertThat(commentaires).hasSize(1);
        assertThat(commentaires.get(0).getContenu()).isEqualTo("Test contenu");
    }

    // Teste la récupération d’un commentaire par son identifiant
    @Test
    void findById_shouldReturnSavedCommentaire() {
        // Création et sauvegarde d'un utilisateur
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        // Création et sauvegarde d'un topic
        Topic topic = new Topic();
        topic.setName("Java");
        topic.setDescription("Langage de programmation");
        topicService.save(topic);

        // Création et sauvegarde d’un article
        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user);
        article.setTopic(topic);
        article = articleRepository.save(article);

        // Création et sauvegarde du commentaire
        Commentaire commentaire = new Commentaire();
        commentaire.setContenu("Autre contenu");
        commentaire.setCreateur(user);
        commentaire.setArticle(article);
        Commentaire saved = commentaireService.save(commentaire);

        // Vérifie que le commentaire est retrouvé par son id
        Optional<Commentaire> found = commentaireService.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getContenu()).isEqualTo("Autre contenu");
    }

    // Teste la suppression d’un commentaire par son identifiant
    @Test
    void deleteById_shouldRemoveCommentaire() {
        // Création et sauvegarde d'un utilisateur
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        // Création et sauvegarde d'un topic
        Topic topic = new Topic();
        topic.setName("Java");
        topic.setDescription("Langage de programmation");
        topicService.save(topic);

        // Création et sauvegarde d’un article
        Article article = new Article();
        article.setTitre("Titre");
        article.setContenu("Contenu");
        article.setAuteur(user);
        article.setTopic(topic);
        article = articleRepository.save(article);

        // Création et sauvegarde du commentaire
        Commentaire commentaire = new Commentaire();
        commentaire.setContenu("Un commentaire");
        commentaire.setCreateur(user);
        commentaire.setArticle(article); // Important !
        commentaire = commentaireRepository.save(commentaire);

        // Teste ensuite la suppression
        commentaireService.deleteById(commentaire.getId());
        assertThat(commentaireRepository.findById(commentaire.getId())).isEmpty();
    }
}