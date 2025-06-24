package com.openclassrooms.mddapi.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentaireTest {

    @Test
    void shouldCreateCommentaireWithAllArgsConstructor() {
        User user = new User();
        Article article = new Article();
        LocalDateTime now = LocalDateTime.now();

        Commentaire commentaire = new Commentaire(
                1L,
                "Contenu du commentaire",
                user,
                article,
                now
        );

        assertThat(commentaire.getId()).isEqualTo(1L);
        assertThat(commentaire.getContenu()).isEqualTo("Contenu du commentaire");
        assertThat(commentaire.getCreateur()).isEqualTo(user);
        assertThat(commentaire.getArticle()).isEqualTo(article);
        assertThat(commentaire.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void shouldSetAndGetFields() {
        Commentaire commentaire = new Commentaire();
        User user = new User();
        Article article = new Article();
        LocalDateTime now = LocalDateTime.now();

        commentaire.setId(2L);
        commentaire.setContenu("Autre contenu");
        commentaire.setCreateur(user);
        commentaire.setArticle(article);
        commentaire.setCreatedAt(now);

        assertThat(commentaire.getId()).isEqualTo(2L);
        assertThat(commentaire.getContenu()).isEqualTo("Autre contenu");
        assertThat(commentaire.getCreateur()).isEqualTo(user);
        assertThat(commentaire.getArticle()).isEqualTo(article);
        assertThat(commentaire.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        User user = new User();
        Article article = new Article();
        LocalDateTime now = LocalDateTime.now();

        Commentaire c1 = new Commentaire(1L, "Contenu", user, article, now);
        Commentaire c2 = new Commentaire(1L, "Contenu", user, article, now);
        Commentaire c3 = new Commentaire(2L, "Autre", user, article, now);

        assertThat(c1).isEqualTo(c2);
        assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
        assertThat(c1).isNotEqualTo(c3);
    }

    @Test
    void toStringShouldContainFields() {
        User user = new User();
        user.setId(10L);
        Article article = new Article();
        article.setId(20L);
        LocalDateTime now = LocalDateTime.now();

        Commentaire commentaire = new Commentaire(1L, "Contenu", user, article, now);

        String toString = commentaire.toString();
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("contenu=Contenu"); // sans quotes
        assertThat(toString).contains("createur=User(id=10");
        assertThat(toString).contains("article=Article(id=20");
    }

    @Test
    void equalsShouldHandleVariousCases() {
        User user1 = new User();
        Article article1 = new Article();
        LocalDateTime now = LocalDateTime.now();

        User user2 = new User();
        user2.setId(10L);
        Article article2 = new Article();
        article2.setId(20L);

        // Tous les champs identiques
        Commentaire c1 = new Commentaire(1L, "Contenu", user1, article1, now);
        Commentaire c2 = new Commentaire(1L, "Contenu", user1, article1, now);

        // Un champ différent (contenu)
        Commentaire c3 = new Commentaire(1L, "Autre contenu", user1, article1, now);

        // Un champ différent (utilisateur)
        Commentaire c4 = new Commentaire(1L, "Contenu", user2, article1, now);

        // Un champ différent (article)
        Commentaire c5 = new Commentaire(1L, "Contenu", user1, article2, now);

        // Un champ différent (date)
        Commentaire c6 = new Commentaire(1L, "Contenu", user1, article1, now.plusSeconds(1));

        // Un champ différent (id)
        Commentaire c7 = new Commentaire(2L, "Contenu", user1, article1, now);

        // égalité avec soi-même
        assertThat(c1).isEqualTo(c1);

        // égalité avec un objet identique (tous les champs identiques)
        assertThat(c1).isEqualTo(c2);

        // inégalité avec un champ différent
        assertThat(c1).isNotEqualTo(c3);
        assertThat(c1).isNotEqualTo(c4);
        assertThat(c1).isNotEqualTo(c5);
        assertThat(c1).isNotEqualTo(c6);

        // inégalité avec un autre id
        assertThat(c1).isNotEqualTo(c7);

        // inégalité avec un objet de type différent
        assertThat(c1).isNotEqualTo("string");

        // inégalité avec null
        assertThat(c1).isNotEqualTo(null);
    }
    @Test
    void equalsShouldHandleNullFields() {
        Commentaire c1 = new Commentaire(null, null, null, null, null);
        Commentaire c2 = new Commentaire(null, null, null, null, null);
        Commentaire c3 = new Commentaire(1L, null, null, null, null);
        Commentaire c4 = new Commentaire(null, "x", null, null, null);
        Commentaire c5 = new Commentaire(null, null, new User(), null, null);
        Commentaire c6 = new Commentaire(null, null, null, new Article(), null);
        Commentaire c7 = new Commentaire(null, null, null, null, LocalDateTime.now());

        // Tous les champs null
        assertThat(c1).isEqualTo(c2);

        // Un champ non null
        assertThat(c1).isNotEqualTo(c3);
        assertThat(c1).isNotEqualTo(c4);
        assertThat(c1).isNotEqualTo(c5);
        assertThat(c1).isNotEqualTo(c6);
        assertThat(c1).isNotEqualTo(c7);
    }

    @Test
    void equalsShouldHandleNullVsNonNullFields() {
        User user = new User();
        Article article = new Article();
        LocalDateTime now = LocalDateTime.now();

        // null vs non-null sur chaque champ
        assertThat(new Commentaire(null, "a", user, article, now))
                .isNotEqualTo(new Commentaire(1L, "a", user, article, now));
        assertThat(new Commentaire(1L, null, user, article, now))
                .isNotEqualTo(new Commentaire(1L, "a", user, article, now));
        assertThat(new Commentaire(1L, "a", null, article, now))
                .isNotEqualTo(new Commentaire(1L, "a", user, article, now));
        assertThat(new Commentaire(1L, "a", user, null, now))
                .isNotEqualTo(new Commentaire(1L, "a", user, article, now));
        assertThat(new Commentaire(1L, "a", user, article, null))
                .isNotEqualTo(new Commentaire(1L, "a", user, article, now));
    }

    @Test
    void hashCodeShouldHandleNullFields() {
        Commentaire c1 = new Commentaire(null, null, null, null, null);
        Commentaire c2 = new Commentaire(null, null, null, null, null);
        assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentTypeAndNull() {
        Commentaire c = new Commentaire();
        assertThat(c).isNotEqualTo("string");
        assertThat(c).isNotEqualTo(null);
    }
}
