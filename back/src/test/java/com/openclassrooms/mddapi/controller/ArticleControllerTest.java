// Classe de test unitaire pour le contrôleur ArticleController
package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleCreateDto;
import com.openclassrooms.mddapi.dto.CommentaireRequestDto;
import com.openclassrooms.mddapi.model.*;
import com.openclassrooms.mddapi.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class ArticleControllerTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private CommentaireRepository commentaireRepository;

    @InjectMocks
    private ArticleController articleController;

    // Méthode utilitaire pour simuler un Principal (utilisateur authentifié)
    private Principal principalMock(String username) {
        return () -> username;
    }

    // Initialise les mocks avant chaque test
    public ArticleControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createArticle_shouldReturnOk() {
        // Vérifie que la création d'un article retourne un statut 200
        ArticleCreateDto dto = new ArticleCreateDto();
        dto.setTitre("Titre");
        dto.setContenu("Contenu");
        dto.setThemeId(1L);

        User user = new User();
        Topic topic = new Topic();

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        Mockito.when(articleRepository.save(any(Article.class))).thenReturn(new Article());

        ResponseEntity<?> response = articleController.createArticle(dto, principalMock("user"));
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getArticlesAbonnes_shouldReturnList() {
        // Vérifie que la récupération des articles des abonnements retourne une liste et un statut 200
        User user = new User();
        Topic topic = new Topic();
        user.setAbonnements(Set.of(topic));
        Mockito.when(userRepository.findWithAbonnementsAndArticlesByUsername("user")).thenReturn(Optional.of(user));
        Article article = new Article();
        article.setCreatedAt(LocalDateTime.now());
        Mockito.when(articleRepository.findByTopicsWithCommentaires(anyList())).thenReturn(List.of(article));

        ResponseEntity<?> response = articleController.getArticlesAbonnes(principalMock("user"));
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void getArticleById_shouldReturnArticle() {
        // Vérifie que la récupération d'un article par son id retourne bien l'article et un statut 200
        Article article = new Article();
        article.setCreatedAt(LocalDateTime.now());
        Mockito.when(articleRepository.findByIdWithCommentaires(1L)).thenReturn(Optional.of(article));

        ResponseEntity<?> response = articleController.getArticleById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void ajouterCommentaire_shouldReturnOk() {
        // Vérifie que l'ajout d'un commentaire retourne un statut 200
        Article article = new Article();
        User user = new User();
        CommentaireRequestDto dto = new CommentaireRequestDto();
        dto.setContenu("Un commentaire");

        Mockito.when(articleRepository.findByIdWithCommentaires(1L)).thenReturn(Optional.of(article));
        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(commentaireRepository.save(any(Commentaire.class))).thenReturn(new Commentaire());

        ResponseEntity<?> response = articleController.ajouterCommentaire(1L, dto, principalMock("user"));
        assertEquals(200, response.getStatusCodeValue());
    }
}