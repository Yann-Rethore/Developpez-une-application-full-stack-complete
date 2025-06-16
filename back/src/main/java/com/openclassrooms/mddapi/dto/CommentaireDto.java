package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.model.Commentaire;
import lombok.Data;

import java.util.Date;

@Data
public class CommentaireDto {
    private Long id;
    private String contenu;
    private String createurUsername;
    private String date;

    public CommentaireDto(Commentaire commentaire) {
        this.id = commentaire.getId();
        this.contenu = commentaire.getContenu();
        this.createurUsername = commentaire.getCreateur().getUsername();
        this.date = commentaire.getCreatedAt().toString();
    }
}
