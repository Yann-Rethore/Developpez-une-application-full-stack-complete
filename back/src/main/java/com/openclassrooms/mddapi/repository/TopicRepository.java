package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("SELECT DISTINCT t FROM Topic t LEFT JOIN FETCH t.abonnes WHERE t.id = :id")
    Optional<Topic> findByIdWithAbonnes(@Param("id") Long id);

    @Query("SELECT t FROM User u JOIN u.abonnements t WHERE u.username = :username")
    List<Topic> findAbonnementsByUsername(@Param("username") String username);

}