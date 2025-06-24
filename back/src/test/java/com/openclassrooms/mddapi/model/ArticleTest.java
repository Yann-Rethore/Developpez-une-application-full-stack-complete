package com.openclassrooms.mddapi.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    @Test
    void shouldCreateArticleWithAllArgsConstructor() {
        Topic topic = new Topic();
        User auteur = new User();
        LocalDateTime now = LocalDateTime.now();

        Article article = new Article(
                1L,
                "Titre",
                "Contenu",
                topic,
                auteur,
                now,
                Collections.emptyList()
        );

        assertThat(article.getId()).isEqualTo(1L);
        assertThat(article.getTitre()).isEqualTo("Titre");
        assertThat(article.getContenu()).isEqualTo("Contenu");
        assertThat(article.getTopic()).isEqualTo(topic);
        assertThat(article.getAuteur()).isEqualTo(auteur);
        assertThat(article.getCreatedAt()).isEqualTo(now);
        assertThat(article.getCommentaires()).isEmpty();
    }

    @Test
    void shouldSetAndGetFields() {
        Article article = new Article();
        Topic topic = new Topic();
        User auteur = new User();

        article.setId(2L);
        article.setTitre("Titre2");
        article.setContenu("Contenu2");
        article.setTopic(topic);
        article.setAuteur(auteur);

        assertThat(article.getId()).isEqualTo(2L);
        assertThat(article.getTitre()).isEqualTo("Titre2");
        assertThat(article.getContenu()).isEqualTo("Contenu2");
        assertThat(article.getTopic()).isEqualTo(topic);
        assertThat(article.getAuteur()).isEqualTo(auteur);
    }

    @Test
    void commentairesListShouldBeMutable() {
        Article article = new Article();
        Commentaire commentaire = new Commentaire();
        article.getCommentaires().add(commentaire);

        assertThat(article.getCommentaires()).hasSize(1);
        assertThat(article.getCommentaires().get(0)).isEqualTo(commentaire);
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        Topic topic = new Topic();
        User auteur = new User();
        LocalDateTime now = LocalDateTime.now();

        Article a1 = new Article(1L, "Titre", "Contenu", topic, auteur, now, Collections.emptyList());
        Article a2 = new Article(1L, "Titre", "Contenu", topic, auteur, now, Collections.emptyList());
        Article a3 = new Article(2L, "Autre", "Diff", topic, auteur, now, Collections.emptyList());

        assertThat(a1).isEqualTo(a2);
        assertThat(a1.hashCode()).isEqualTo(a2.hashCode());
        assertThat(a1).isNotEqualTo(a3);
    }

    @Test
    void toStringShouldContainFields() {
        Topic topic = new Topic();
        topic.setId(10L);
        User auteur = new User();
        auteur.setId(20L);
        LocalDateTime now = LocalDateTime.now();

        Article article = new Article(1L, "Titre", "Contenu", topic, auteur, now, Collections.emptyList());

        String toString = article.toString();
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("titre=Titre"); // sans quotes
        assertThat(toString).contains("topic=Topic(id=10");
        assertThat(toString).contains("auteur=User(id=20");
    }

    @Test
    void equalsShouldHandleVariousCases() {
        Topic topic = new Topic();
        User auteur = new User();
        LocalDateTime now = LocalDateTime.now();

        Article a1 = new Article(1L, "Titre", "Contenu", topic, auteur, now, Collections.emptyList());
        Article a2 = new Article(1L, "Titre", "Contenu", topic, auteur, now, Collections.emptyList());
        Article a3 = new Article(2L, "Titre", "Contenu", topic, auteur, now, Collections.emptyList());

        // égalité avec soi-même
        assertThat(a1).isEqualTo(a1);

        // égalité avec un objet identique
        assertThat(a1).isEqualTo(a2);

        // inégalité avec un objet de type différent
        assertThat(a1).isNotEqualTo("string");

        // inégalité avec null
        assertThat(a1).isNotEqualTo(null);

        // inégalité avec un autre id
        assertThat(a1).isNotEqualTo(a3);
    }

    @Test
    void equalsShouldHandleNullFields() {
        Article a1 = new Article(null, null, null, null, null, null, null);
        Article a2 = new Article(null, null, null, null, null, null, null);
        Article a3 = new Article(1L, null, null, null, null, null, null);
        Article a4 = new Article(null, "x", null, null, null, null, null);
        Article a5 = new Article(null, null, "y", null, null, null, null);
        Article a6 = new Article(null, null, null, new Topic(), null, null, null);
        Article a7 = new Article(null, null, null, null, new User(), null, null);
        Article a8 = new Article(null, null, null, null, null, LocalDateTime.now(), null);
        Article a9 = new Article(null, null, null, null, null, null, Collections.singletonList(new Commentaire()));

        // Tous les champs null
        assertThat(a1).isEqualTo(a2);

        // Un champ non null
        assertThat(a1).isNotEqualTo(a3);
        assertThat(a1).isNotEqualTo(a4);
        assertThat(a1).isNotEqualTo(a5);
        assertThat(a1).isNotEqualTo(a6);
        assertThat(a1).isNotEqualTo(a7);
        assertThat(a1).isNotEqualTo(a8);
        assertThat(a1).isNotEqualTo(a9);
    }

    @Test
    void equalsShouldHandleNullVsNonNullFields() {
        Topic topic = new Topic();
        User auteur = new User();
        LocalDateTime now = LocalDateTime.now();
        Commentaire commentaire = new Commentaire();

        assertThat(new Article(null, "a", "b", topic, auteur, now, Collections.emptyList()))
                .isNotEqualTo(new Article(1L, "a", "b", topic, auteur, now, Collections.emptyList()));
        assertThat(new Article(1L, null, "b", topic, auteur, now, Collections.emptyList()))
                .isNotEqualTo(new Article(1L, "a", "b", topic, auteur, now, Collections.emptyList()));
        assertThat(new Article(1L, "a", null, topic, auteur, now, Collections.emptyList()))
                .isNotEqualTo(new Article(1L, "a", "b", topic, auteur, now, Collections.emptyList()));
        assertThat(new Article(1L, "a", "b", null, auteur, now, Collections.emptyList()))
                .isNotEqualTo(new Article(1L, "a", "b", topic, auteur, now, Collections.emptyList()));
        assertThat(new Article(1L, "a", "b", topic, null, now, Collections.emptyList()))
                .isNotEqualTo(new Article(1L, "a", "b", topic, auteur, now, Collections.emptyList()));
        assertThat(new Article(1L, "a", "b", topic, auteur, null, Collections.emptyList()))
                .isNotEqualTo(new Article(1L, "a", "b", topic, auteur, now, Collections.emptyList()));
        assertThat(new Article(1L, "a", "b", topic, auteur, now, null))
                .isNotEqualTo(new Article(1L, "a", "b", topic, auteur, now, Collections.singletonList(commentaire)));
    }

    @Test
    void hashCodeShouldHandleNullFields() {
        Article a1 = new Article(null, null, null, null, null, null, null);
        Article a2 = new Article(null, null, null, null, null, null, null);
        assertThat(a1.hashCode()).isEqualTo(a2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentTypeAndNull() {
        Article a = new Article();
        assertThat(a).isNotEqualTo("string");
        assertThat(a).isNotEqualTo(null);
    }

    @Test
    void toStringShouldContainAllFieldsEvenIfNull() {
        Article article = new Article();
        String str = article.toString();
        assertThat(str).contains("id=");
        assertThat(str).contains("titre=");
        assertThat(str).contains("contenu=");
        assertThat(str).contains("topic=");
        assertThat(str).contains("auteur=");
        assertThat(str).contains("createdAt=");
        assertThat(str).contains("commentaires=");
    }
}