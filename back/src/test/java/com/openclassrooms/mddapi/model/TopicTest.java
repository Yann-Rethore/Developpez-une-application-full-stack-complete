package com.openclassrooms.mddapi.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class TopicTest {

    @Test
    void shouldCreateTopicWithAllArgsConstructor() {
        Article article = new Article();
        User user = new User();

        Topic topic = new Topic(
                1L,
                "Nom",
                "Description",
                new ArrayList<>(),
                new HashSet<>()
        );
        topic.getArticles().add(article);
        topic.getAbonnes().add(user);

        assertThat(topic.getId()).isEqualTo(1L);
        assertThat(topic.getName()).isEqualTo("Nom");
        assertThat(topic.getDescription()).isEqualTo("Description");
        assertThat(topic.getArticles()).contains(article);
        assertThat(topic.getAbonnes()).contains(user);
    }

    @Test
    void shouldSetAndGetFields() {
        Topic topic = new Topic();
        topic.setId(2L);
        topic.setName("Java");
        topic.setDescription("Langage");

        assertThat(topic.getId()).isEqualTo(2L);
        assertThat(topic.getName()).isEqualTo("Java");
        assertThat(topic.getDescription()).isEqualTo("Langage");
    }

    @Test
    void articlesListShouldBeMutable() {
        Topic topic = new Topic();
        Article article = new Article();
        topic.getArticles().add(article);

        assertThat(topic.getArticles()).hasSize(1);
        assertThat(topic.getArticles().get(0)).isEqualTo(article);
    }

    @Test
    void abonnesSetShouldBeMutable() {
        Topic topic = new Topic();
        User user = new User();
        topic.getAbonnes().add(user);

        assertThat(topic.getAbonnes()).hasSize(1);
        assertThat(topic.getAbonnes().contains(user)).isTrue();
    }

    @Test
    void equalsAndHashCodeShouldWorkOnId() {
        Topic t1 = new Topic();
        t1.setId(10L);
        Topic t2 = new Topic();
        t2.setId(10L);

        assertThat(t1).isEqualTo(t2);
        assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
    }

    @Test
    void equalsShouldWorkOnId() {
        Topic t1 = new Topic(5L, "Nom", "Desc", new ArrayList<>(), new HashSet<>());
        Topic t2 = new Topic(5L, "Autre", "AutreDesc", new ArrayList<>(), new HashSet<>());
        Topic t3 = new Topic(6L, "Nom", "Desc", new ArrayList<>(), new HashSet<>());

        assertThat(t1).isEqualTo(t2);
        assertThat(t1).isNotEqualTo(t3);
    }

    @Test
    void toStringShouldContainFields() {
        Topic topic = new Topic(7L, "Spring", "Framework", new ArrayList<>(), new HashSet<>());
        String toString = topic.toString();

        assertThat(toString).contains("id=7");
        assertThat(toString).contains("name=Spring"); // sans quotes
        assertThat(toString).contains("description=Framework"); // sans quotes
    }

    @Test
    void equalsShouldHandleVariousCases() {
        Topic t1 = new Topic(1L, "Nom", "Desc", new ArrayList<>(), new HashSet<>());
        Topic t2 = new Topic(1L, "Autre", "AutreDesc", new ArrayList<>(), new HashSet<>());
        Topic t3 = new Topic(2L, "Nom", "Desc", new ArrayList<>(), new HashSet<>());

        // égalité avec soi-même
        assertThat(t1).isEqualTo(t1);

        // égalité avec un objet identique (même id)
        assertThat(t1).isEqualTo(t2);

        // inégalité avec un objet de type différent
        assertThat(t1).isNotEqualTo("string");

        // inégalité avec null
        assertThat(t1).isNotEqualTo(null);

        // inégalité avec un autre id
        assertThat(t1).isNotEqualTo(t3);
    }
}
