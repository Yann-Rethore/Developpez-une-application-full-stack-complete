// Classe de test unitaire pour TopicService
package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.dto.TopicDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository; // Mock du repository

    @InjectMocks
    private TopicService topicService; // Service à tester

    // Initialise les mocks avant chaque test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Vérifie que findAll retourne tous les topics du repository
    @Test
    void findAll_shouldReturnAllTopics() {
        List<Topic> topics = List.of(new Topic(), new Topic());
        when(topicRepository.findAll()).thenReturn(topics);

        List<Topic> result = topicService.findAll();

        assertThat(result).hasSize(2);
        verify(topicRepository).findAll();
    }

    // Vérifie que findById retourne le topic si présent
    @Test
    void findById_shouldReturnTopicIfExists() {
        Topic topic = new Topic();
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        Optional<Topic> result = topicService.findById(1L);

        assertThat(result).isPresent();
        verify(topicRepository).findById(1L);
    }

    // Vérifie que save sauvegarde et retourne le topic
    @Test
    void save_shouldSaveAndReturnTopic() {
        Topic topic = new Topic();
        when(topicRepository.save(topic)).thenReturn(topic);

        Topic result = topicService.save(topic);

        assertThat(result).isEqualTo(topic);
        verify(topicRepository).save(topic);
    }

    // Vérifie que deleteById appelle la suppression sur le repository
    @Test
    void deleteById_shouldCallRepositoryDelete() {
        topicService.deleteById(1L);

        verify(topicRepository).deleteById(1L);
    }

    // Vérifie que getAllTopics retourne une liste de TopicDto correctement mappée
    @Test
    void getAllTopics_shouldReturnListOfTopicDto() {
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Test");
        topic.setDescription("Desc");
        when(topicRepository.findAll()).thenReturn(List.of(topic));

        List<TopicDto> dtos = topicService.getAllTopics();

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(0).getName()).isEqualTo("Test");
        assertThat(dtos.get(0).getDescription()).isEqualTo("Desc");
        verify(topicRepository).findAll();
    }
}