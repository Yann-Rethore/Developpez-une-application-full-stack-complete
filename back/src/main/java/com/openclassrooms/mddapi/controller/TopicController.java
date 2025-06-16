package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.TopicService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/themes")
public class TopicController {


    @Autowired
    private TopicService topicService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping
    public List<TopicDto> getAllTopics() {
        return topicService.getAllTopics();
    }

    @Transactional
    @GetMapping("/me/subscriptions")
    public ResponseEntity<List<Long>> getUserSubscriptions(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String username = principal.getName();
        // User user = userRepository.findByUsernameWithAbonnements(username).orElseThrow();
        List<Topic> topics = topicRepository.findAbonnementsByUsername(username);
        List<Long> ids = topics.stream().map(Topic::getId).toList();

        System.out.println("Utilisateur connecté : " + username);
        System.out.println("IDs des topics abonnés : " + ids);

        return ResponseEntity.ok(ids);
    }

    @Transactional
    @GetMapping("/subscribe/{topicId}")
    public ResponseEntity<?> subscribeToTopic(@PathVariable Long topicId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findWithAbonnementsByUsername(username).orElseThrow();
        Topic topic = topicRepository.findByIdWithAbonnes(topicId).orElseThrow();

        user.getAbonnements().add(topic);
        topic.getAbonnes().add(user);
        topicRepository.save(topic);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @Transactional
    @GetMapping("/unsubscribe/{topicId}")
    public ResponseEntity<?> unsubscribeFromTopic(@PathVariable Long topicId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findWithAbonnementsByUsername(username).orElseThrow();
        Topic topic = topicRepository.findByIdWithAbonnes(topicId).orElseThrow();


        user.getAbonnements().remove(topic);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
