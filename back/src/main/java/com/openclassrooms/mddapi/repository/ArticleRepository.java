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

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTopicIdIn(Set<Long> topicIds);
    @Query("SELECT DISTINCT a FROM Article a LEFT JOIN FETCH a.commentaires WHERE a.topic IN :topics")
    List<Article> findByTopicsWithCommentaires(@Param("topics") List<Topic> topics);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.commentaires WHERE a.id = :id")
    Optional<Article> findByIdWithCommentaires(@Param("id") Long id);
}