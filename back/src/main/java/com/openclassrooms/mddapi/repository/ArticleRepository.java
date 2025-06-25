// Interface de repository pour l'entité Article, gère l'accès aux données des articles
package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Topic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository // Indique que cette interface est un composant Spring pour l'accès aux données
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Récupère tous les articles dont le thème a un identifiant présent dans la liste fournie
    List<Article> findByTopicIdIn(Set<Long> topicIds);

    // Récupère tous les articles associés à une liste de thèmes, en chargeant aussi leurs commentaires (évite le N+1)
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.commentaires WHERE a.topic IN :topics")
    List<Article> findByTopicsWithCommentaires(@Param("topics") List<Topic> topics);

    // Récupère un article par son identifiant, en chargeant aussi ses commentaires
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.commentaires WHERE a.id = :id")
    Optional<Article> findByIdWithCommentaires(@Param("id") Long id);
}