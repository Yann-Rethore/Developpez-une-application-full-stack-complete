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

@DataJpaTest
@Import({CommentaireService.class, TopicService.class})
@ActiveProfiles("test")
class CommentaireServiceIT {

    @Autowired
    private CommentaireService commentaireService;

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TopicService topicService;

    @Test
    void saveAndFindAll_shouldPersistAndReturnCommentaires() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

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

        List<Commentaire> commentaires = commentaireService.findAll();
        assertThat(commentaires).hasSize(1);
        assertThat(commentaires.get(0).getContenu()).isEqualTo("Test contenu");
    }

    @Test
    void findById_shouldReturnSavedCommentaire() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Topic topic = new Topic();
        topic.setName("Java");
        topic.setDescription("Langage de programmation");
        topicService.save(topic);

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

        Optional<Commentaire> found = commentaireService.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getContenu()).isEqualTo("Autre contenu");
    }

    @Test
    void deleteById_shouldRemoveCommentaire() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

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