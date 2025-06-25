// Service pour la gestion des thèmes (topics)
package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.dto.TopicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Indique que cette classe est un service Spring
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired // Injection du repository via le constructeur
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    // Récupère tous les thèmes
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    // Récupère un thème par son identifiant
    public Optional<Topic> findById(Long id) {
        return topicRepository.findById(id);
    }

    // Sauvegarde ou met à jour un thème
    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    // Supprime un thème par son identifiant
    public void deleteById(Long id) {
        topicRepository.deleteById(id);
    }

    // Récupère tous les thèmes et les convertit en DTO pour l'affichage
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