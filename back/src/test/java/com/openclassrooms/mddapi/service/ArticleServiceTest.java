package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnAllArticles() {
        List<Article> articles = List.of(new Article(), new Article());
        when(articleRepository.findAll()).thenReturn(articles);

        List<Article> result = articleService.findAll();

        assertThat(result).hasSize(2);
        verify(articleRepository).findAll();
    }

    @Test
    void findById_shouldReturnArticleIfExists() {
        Article article = new Article();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Optional<Article> result = articleService.findById(1L);

        assertThat(result).isPresent();
        verify(articleRepository).findById(1L);
    }

    @Test
    void save_shouldSaveAndReturnArticle() {
        Article article = new Article();
        when(articleRepository.save(article)).thenReturn(article);

        Article result = articleService.save(article);

        assertThat(result).isEqualTo(article);
        verify(articleRepository).save(article);
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        articleService.deleteById(1L);

        verify(articleRepository).deleteById(1L);
    }

    @Test
    void findArticlesByAbonnements_shouldReturnArticlesForUserTopics() {
        Topic topic1 = new Topic();
        topic1.setId(1L);
        Topic topic2 = new Topic();
        topic2.setId(2L);

        User user = new User();
        user.setAbonnements(Set.of(topic1, topic2));

        List<Article> articles = List.of(new Article());
        when(articleRepository.findByTopicIdIn(Set.of(1L, 2L))).thenReturn(articles);

        List<Article> result = articleService.findArticlesByAbonnements(user);

        assertThat(result).isEqualTo(articles);
        verify(articleRepository).findByTopicIdIn(Set.of(1L, 2L));
    }
}