package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Commentaire;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;

    @Autowired
    public CommentaireService(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    public List<Commentaire> findAll() {
        return commentaireRepository.findAll();
    }

    public Optional<Commentaire> findById(Long id) {
        return commentaireRepository.findById(id);
    }

    public Commentaire save(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    public void deleteById(Long id) {
        commentaireRepository.deleteById(id);
    }
}