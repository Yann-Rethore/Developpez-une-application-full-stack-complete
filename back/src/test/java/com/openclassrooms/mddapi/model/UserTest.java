package com.openclassrooms.mddapi.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void shouldCreateUserWithAllArgsConstructor() {
        Topic topic = new Topic();
        HashSet<Topic> abonnements = new HashSet<>();
        abonnements.add(topic);

        User user = new User(
                1L,
                "mail@mail.com",
                "password",
                "username",
                abonnements
        );

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("mail@mail.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(user.getAbonnements()).contains(topic);
    }

    @Test
    void shouldSetAndGetFields() {
        User user = new User();
        user.setId(42L);
        user.setEmail("test@x.fr");
        user.setPassword("pwd");
        user.setUsername("toto");
        user.setAbonnements(Set.of());

        assertThat(user.getId()).isEqualTo(42L);
        assertThat(user.getEmail()).isEqualTo("test@x.fr");
        assertThat(user.getPassword()).isEqualTo("pwd");
        assertThat(user.getUsername()).isEqualTo("toto");
        assertThat(user.getAbonnements()).isEmpty();
    }

    @Test
    void abonnementsSetShouldBeMutable() {
        User user = new User();
        Topic topic = new Topic();
        user.getAbonnements().add(topic);

        assertThat(user.getAbonnements()).hasSize(1);
        assertThat(user.getAbonnements()).contains(topic);
    }

    @Test
    void hashCodeShouldBeConsistentForEqualUsers() {
        User u1 = new User(1L, "mail@test.com", "pass", "user", null);
        User u2 = new User(1L, "mail@test.com", "pass", "user", null);
        User u3 = new User(2L, "other@mail.com", "pass2", "other", null);

        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
        assertThat(u1.hashCode()).isNotEqualTo(u3.hashCode());
    }

    @Test
    void toStringShouldContainFields() {
        User user = new User(1L, "mail@test.com", "pass", "user", null);

        String toString = user.toString();
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("email=mail@test.com"); // sans quotes
        assertThat(toString).contains("password=pass");
        assertThat(toString).contains("username=user"); // sans quotes
    }


    @Test
    void equalsAndHashCodeShouldCoverAllBranches() {
        Set<Topic> emptySet = Set.of();
        Set<Topic> setWithOne = Set.of(new Topic());

        User u1 = new User(1L, "a@b.com", "pass", "user", emptySet);
        User u2 = new User(1L, "a@b.com", "pass", "user", emptySet);
        User u3 = new User(2L, "a@b.com", "pass", "user", emptySet);
        User u4 = new User(1L, "x@b.com", "pass", "user", emptySet);
        User u5 = new User(1L, "a@b.com", "other", "user", emptySet);
        User u6 = new User(1L, "a@b.com", "pass", "other", emptySet);
        User u7 = new User(1L, "a@b.com", "pass", "user", setWithOne);
        User u8 = new User(1L, "a@b.com", "pass", "user", null);

        // égalité avec soi-même
        assertThat(u1).isEqualTo(u1);

        // égalité avec un objet identique
        assertThat(u1).isEqualTo(u2);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());

        // inégalité sur chaque champ
        assertThat(u1).isNotEqualTo(u3); // id
        assertThat(u1).isNotEqualTo(u4); // email
        assertThat(u1).isNotEqualTo(u5); // password
        assertThat(u1).isNotEqualTo(u6); // username
        assertThat(u1).isNotEqualTo(u7); // abonnements
        assertThat(u1).isNotEqualTo(u8); // abonnements null vs vide

        // inégalité avec un objet d’un autre type
        assertThat(u1).isNotEqualTo("string");

        // inégalité avec null
        assertThat(u1).isNotEqualTo(null);
    }

    @Test
    void equalsShouldHandleNullFields() {
        User u1 = new User(null, null, null, null, null);
        User u2 = new User(null, null, null, null, null);
        User u3 = new User(1L, null, null, null, null);
        User u4 = new User(null, "a@b.com", null, null, null);
        User u5 = new User(null, null, "pass", null, null);
        User u6 = new User(null, null, null, "user", null);
        User u7 = new User(null, null, null, null, Set.of(new Topic()));

        // Tous les champs null
        assertThat(u1).isEqualTo(u2);

        // Un champ non null
        assertThat(u1).isNotEqualTo(u3);
        assertThat(u1).isNotEqualTo(u4);
        assertThat(u1).isNotEqualTo(u5);
        assertThat(u1).isNotEqualTo(u6);
        assertThat(u1).isNotEqualTo(u7);

        // Null vs non-null sur abonnements
        User u8 = new User(1L, "a@b.com", "pass", "user", null);
        User u9 = new User(1L, "a@b.com", "pass", "user", Set.of());
        assertThat(u8).isNotEqualTo(u9);
    }

    @Test
    void hashCodeShouldHandleNullFields() {
        User u1 = new User(null, null, null, null, null);
        User u2 = new User(null, null, null, null, null);
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }
}