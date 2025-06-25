package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service // Indique que cette classe est un service Spring
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired // Injection du repository via le constructeur
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // Récupère tous les articles
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    // Récupère un article par son identifiant
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    // Sauvegarde ou met à jour un article
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    // Supprime un article par son identifiant
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    // Récupère les articles liés aux thèmes auxquels l'utilisateur est abonné
    public List<Article> findArticlesByAbonnements(User utilisateur) {
        Set<Long> topicIds = utilisateur.getAbonnements().stream()
                .map(topic -> topic.getId())
                .collect(java.util.stream.Collectors.toSet());
        return articleRepository.findByTopicIdIn(topicIds);
    }
}