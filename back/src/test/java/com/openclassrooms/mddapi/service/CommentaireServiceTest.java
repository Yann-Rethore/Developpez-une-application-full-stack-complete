// Classe de test unitaire pour CommentaireService
package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Commentaire;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CommentaireServiceTest {

    @Mock
    private CommentaireRepository commentaireRepository; // Mock du repository

    @InjectMocks
    private CommentaireService commentaireService; // Service à tester

    // Initialise les mocks avant chaque test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Vérifie que findAll retourne tous les commentaires du repository
    @Test
    void findAll_shouldReturnAllCommentaires() {
        List<Commentaire> commentaires = List.of(new Commentaire(), new Commentaire());
        when(commentaireRepository.findAll()).thenReturn(commentaires);

        List<Commentaire> result = commentaireService.findAll();

        assertThat(result).hasSize(2);
        verify(commentaireRepository).findAll();
    }

    // Vérifie que findById retourne le commentaire si présent
    @Test
    void findById_shouldReturnCommentaireIfExists() {
        Commentaire commentaire = new Commentaire();
        when(commentaireRepository.findById(1L)).thenReturn(Optional.of(commentaire));

        Optional<Commentaire> result = commentaireService.findById(1L);

        assertThat(result).isPresent();
        verify(commentaireRepository).findById(1L);
    }

    // Vérifie que save sauvegarde et retourne le commentaire
    @Test
    void save_shouldSaveAndReturnCommentaire() {
        Commentaire commentaire = new Commentaire();
        when(commentaireRepository.save(commentaire)).thenReturn(commentaire);

        Commentaire result = commentaireService.save(commentaire);

        assertThat(result).isEqualTo(commentaire);
        verify(commentaireRepository).save(commentaire);
    }

    // Vérifie que deleteById appelle la suppression sur le repository
    @Test
    void deleteById_shouldCallRepositoryDelete() {
        commentaireService.deleteById(1L);

        verify(commentaireRepository).deleteById(1L);
    }
}