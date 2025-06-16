package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = "abonnements")
    Optional<User> findWithAbonnementsByUsername(String username);

    @EntityGraph(attributePaths = {"abonnements", "abonnements.articles"})
    Optional<User> findWithAbonnementsAndArticlesByUsername(String username);
}
