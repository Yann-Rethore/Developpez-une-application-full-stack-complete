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
    private CommentaireRepository commentaireRepository;

    @InjectMocks
    private CommentaireService commentaireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnAllCommentaires() {
        List<Commentaire> commentaires = List.of(new Commentaire(), new Commentaire());
        when(commentaireRepository.findAll()).thenReturn(commentaires);

        List<Commentaire> result = commentaireService.findAll();

        assertThat(result).hasSize(2);
        verify(commentaireRepository).findAll();
    }

    @Test
    void findById_shouldReturnCommentaireIfExists() {
        Commentaire commentaire = new Commentaire();
        when(commentaireRepository.findById(1L)).thenReturn(Optional.of(commentaire));

        Optional<Commentaire> result = commentaireService.findById(1L);

        assertThat(result).isPresent();
        verify(commentaireRepository).findById(1L);
    }

    @Test
    void save_shouldSaveAndReturnCommentaire() {
        Commentaire commentaire = new Commentaire();
        when(commentaireRepository.save(commentaire)).thenReturn(commentaire);

        Commentaire result = commentaireService.save(commentaire);

        assertThat(result).isEqualTo(commentaire);
        verify(commentaireRepository).save(commentaire);
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        commentaireService.deleteById(1L);

        verify(commentaireRepository).deleteById(1L);
    }
}
