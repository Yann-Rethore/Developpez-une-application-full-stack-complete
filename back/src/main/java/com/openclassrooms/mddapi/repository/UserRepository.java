// Interface de repository pour l'entité User, gère l'accès aux données des utilisateurs
package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Recherche un utilisateur par son email
    Optional<User> findByEmail(String email);

    // Recherche un utilisateur par son nom d'utilisateur
    Optional<User> findByUsername(String username);

    // Recherche un utilisateur par son nom d'utilisateur et charge ses abonnements (évite le N+1)
    @EntityGraph(attributePaths = "abonnements")
    Optional<User> findWithAbonnementsByUsername(String username);

    // Recherche un utilisateur par son nom d'utilisateur et charge ses abonnements et les articles de ces abonnements
    @EntityGraph(attributePaths = {"abonnements", "abonnements.articles"})
    Optional<User> findWithAbonnementsAndArticlesByUsername(String username);
}