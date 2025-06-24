package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TopicService.class)
@ActiveProfiles("test")
class TopicServiceIT {

    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    void saveAndFindAll_shouldPersistAndReturnTopics() {
        Topic topic = new Topic();
        topic.setName("Java");
        topic.setDescription("Langage de programmation");
        topicService.save(topic);

        List<Topic> topics = topicService.findAll();
        assertThat(topics).hasSize(1);
        assertThat(topics.get(0).getName()).isEqualTo("Java");
    }

    @Test
    void findById_shouldReturnSavedTopic() {
        Topic topic = new Topic();
        topic.setName("Spring");
        topic.setDescription("Framework Java");
        Topic saved = topicService.save(topic);

        Optional<Topic> found = topicService.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Spring");
    }

    @Test
    void deleteById_shouldRemoveTopic() {
        Topic topic = new Topic();
        topic.setName("Test");
        topic.setDescription("À supprimer");
        Topic saved = topicService.save(topic);

        topicService.deleteById(saved.getId());
        assertThat(topicRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void getAllTopics_shouldReturnListOfTopicDto() {
        Topic topic = new Topic();
        topic.setName("API");
        topic.setDescription("Description API");
        topicService.save(topic);

        List<TopicDto> dtos = topicService.getAllTopics();
        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getName()).isEqualTo("API");
        assertThat(dtos.get(0).getDescription()).isEqualTo("Description API");
    }
}