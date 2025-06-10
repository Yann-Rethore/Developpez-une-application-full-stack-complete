package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.dto.TopicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    public Optional<Topic> findById(Long id) {
        return topicRepository.findById(id);
    }

    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    public void deleteById(Long id) {
        topicRepository.deleteById(id);
    }

    public List<TopicDto> getAllTopics() {
        return topicRepository.findAll().stream().map(topic -> {
            TopicDto dto = new TopicDto();
            dto.setId(topic.getId());
            dto.setName(topic.getName());
            dto.setDescription(topic.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }
}