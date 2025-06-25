// Interface de repository pour l'entité Topic, gère l'accès aux données des thèmes
package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository // Indique que cette interface est un composant Spring pour l'accès aux données
public interface TopicRepository extends JpaRepository<Topic, Long> {

    // Récupère un thème par son identifiant, en chargeant aussi la liste de ses abonnés (évite le N+1)
    @Query("SELECT DISTINCT t FROM Topic t LEFT JOIN FETCH t.abonnes WHERE t.id = :id")
    Optional<Topic> findByIdWithAbonnes(@Param("id") Long id);

    // Récupère la liste des thèmes auxquels un utilisateur (par son username) est abonné
    @Query("SELECT t FROM User u JOIN u.abonnements t WHERE u.username = :username")
    List<Topic> findAbonnementsByUsername(@Param("username") String username);

}