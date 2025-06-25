// Service pour la gestion des commentaires (logique métier)
package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Commentaire;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indique que cette classe est un service Spring
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;

    @Autowired // Injection du repository via le constructeur
    public CommentaireService(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    // Récupère tous les commentaires
    public List<Commentaire> findAll() {
        return commentaireRepository.findAll();
    }

    // Récupère un commentaire par son identifiant
    public Optional<Commentaire> findById(Long id) {
        return commentaireRepository.findById(id);
    }

    // Sauvegarde ou met à jour un commentaire
    public Commentaire save(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    // Supprime un commentaire par son identifiant
    public void deleteById(Long id) {
        commentaireRepository.deleteById(id);
    }
}