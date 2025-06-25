package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleCreateDto;
import com.openclassrooms.mddapi.dto.CommentaireRequestDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Commentaire;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.dto.ArticleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CommentaireRepository commentaireRepository;

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody ArticleCreateDto dto, Principal principal) {
        String username = principal.getName();
        User auteur = userRepository.findByUsername(username).orElseThrow();
        Topic theme = topicRepository.findById(dto.getThemeId()).orElseThrow();

        Article article = new Article();
        article.setTitre(dto.getTitre());
        article.setContenu(dto.getContenu());
        article.setAuteur(auteur);
        article.setTopic(theme);


        articleRepository.save(article);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/abonnes")
    public ResponseEntity<List<ArticleDto>> getArticlesAbonnes(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findWithAbonnementsAndArticlesByUsername(username).orElseThrow();
        List<Topic> abonnements = new ArrayList<>(user.getAbonnements());
        List<Article> articles = articleRepository.findByTopicsWithCommentaires(abonnements);
        List<ArticleDto> dtos = articles.stream().map(ArticleDto::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
        Article article = articleRepository.findByIdWithCommentaires(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(new ArticleDto(article));
    }

    @PostMapping("/{id}/commentaire")
    public ResponseEntity<?> ajouterCommentaire(
            @PathVariable("id") Long id,
            @RequestBody CommentaireRequestDto dto,
            Principal principal) {

        String username = principal.getName();
        Article article = articleRepository.findByIdWithCommentaires(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Commentaire commentaire = new Commentaire();
        commentaire.setContenu(dto.getContenu());
        commentaire.setArticle(article);
        commentaire.setCreateur(user);
        commentaireRepository.save(commentaire);

        return ResponseEntity.ok().build();
    }
}
