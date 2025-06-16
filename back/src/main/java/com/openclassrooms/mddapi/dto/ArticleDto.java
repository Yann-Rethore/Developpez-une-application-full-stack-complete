package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Commentaire;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String titre;
    private String contenu;
    private String date;
    private String auteurUsername;
    private String themeName;
    private List<CommentaireDto> commentaires;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.titre = article.getTitre();
        this.contenu = article.getContenu();
        this.date = article.getCreatedAt().toString();
        this.auteurUsername = article.getAuteur() != null ? article.getAuteur().getUsername() : null;
        this.themeName = article.getTopic() != null ? article.getTopic().getName() : null;
        this.commentaires = article.getCommentaires()
                .stream()
                .map(CommentaireDto::new)
                .collect(Collectors.toList());
    }
}
