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
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnAllTopics() {
        List<Topic> topics = List.of(new Topic(), new Topic());
        when(topicRepository.findAll()).thenReturn(topics);

        List<Topic> result = topicService.findAll();

        assertThat(result).hasSize(2);
        verify(topicRepository).findAll();
    }

    @Test
    void findById_shouldReturnTopicIfExists() {
        Topic topic = new Topic();
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        Optional<Topic> result = topicService.findById(1L);

        assertThat(result).isPresent();
        verify(topicRepository).findById(1L);
    }

    @Test
    void save_shouldSaveAndReturnTopic() {
        Topic topic = new Topic();
        when(topicRepository.save(topic)).thenReturn(topic);

        Topic result = topicService.save(topic);

        assertThat(result).isEqualTo(topic);
        verify(topicRepository).save(topic);
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        topicService.deleteById(1L);

        verify(topicRepository).deleteById(1L);
    }

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