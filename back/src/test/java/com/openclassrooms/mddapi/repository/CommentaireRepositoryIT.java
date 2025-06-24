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

@DataJpaTest
class CommentaireRepositoryIT {

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    void shouldSaveAndFindCommentaire() {
        Topic topic = topicRepository.save(new Topic(null, "T", "D", null, null));
        User user = userRepository.save(new User(null, "mail@mail.com", "pass", "user", null));
        Article article = articleRepository.save(new Article(null, "Titre", "Contenu", topic, user, LocalDateTime.now(), null));
        Commentaire commentaire = new Commentaire(null, "Contenu commentaire", user, article, LocalDateTime.now());

        Commentaire saved = commentaireRepository.save(commentaire);

        Optional<Commentaire> found = commentaireRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getContenu()).isEqualTo("Contenu commentaire");
        assertThat(found.get().getArticle()).isEqualTo(article);
        assertThat(found.get().getCreateur()).isEqualTo(user);
    }

    @Test
    void shouldDeleteCommentaire() {
        Topic topic = topicRepository.save(new Topic(null, "T2", "D2", null, null));
        User user = userRepository.save(new User(null, "mail2@mail.com", "pass", "user2", null));
        Article article = articleRepository.save(new Article(null, "Titre2", "Contenu2", topic, user, LocalDateTime.now(), null));
        Commentaire commentaire = commentaireRepository.save(new Commentaire(null, "À supprimer", user, article, LocalDateTime.now()));

        commentaireRepository.deleteById(commentaire.getId());

        assertThat(commentaireRepository.findById(commentaire.getId())).isNotPresent();
    }
}