// Contrôleur REST pour la gestion des thèmes (topics) et des abonnements utilisateur
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

@RestController // Indique que cette classe est un contrôleur REST
@RequestMapping("/api/themes") // Préfixe pour tous les endpoints liés aux thèmes
public class TopicController {

    @Autowired
    private TopicService topicService; // Service métier pour les thèmes

    @Autowired
    private UserRepository userRepository; // Accès aux utilisateurs

    @Autowired
    private TopicRepository topicRepository; // Accès aux thèmes

    // Endpoint GET /api/themes pour récupérer tous les thèmes
    @GetMapping
    public List<TopicDto> getAllTopics() {
        return topicService.getAllTopics();
    }

    // Endpoint GET /api/themes/me/subscriptions pour récupérer les IDs des thèmes auxquels l'utilisateur est abonné
    @Transactional
    @GetMapping("/me/subscriptions")
    public ResponseEntity<List<Long>> getUserSubscriptions(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build(); // Retourne 401 si l'utilisateur n'est pas authentifié
        }
        String username = principal.getName();
        List<Topic> topics = topicRepository.findAbonnementsByUsername(username);
        List<Long> ids = topics.stream().map(Topic::getId).toList();

        System.out.println("Utilisateur connecté : " + username);
        System.out.println("IDs des topics abonnés : " + ids);

        return ResponseEntity.ok(ids);
    }

    // Endpoint GET /api/themes/subscribe/{topicId} pour s'abonner à un thème
    @Transactional
    @GetMapping("/subscribe/{topicId}")
    public ResponseEntity<?> subscribeToTopic(@PathVariable Long topicId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findWithAbonnementsByUsername(username).orElseThrow();
        Topic topic = topicRepository.findByIdWithAbonnes(topicId).orElseThrow();

        user.getAbonnements().add(topic); // Ajoute le thème aux abonnements de l'utilisateur
        topic.getAbonnes().add(user);     // Ajoute l'utilisateur à la liste des abonnés du thème
        topicRepository.save(topic);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    // Endpoint GET /api/themes/unsubscribe/{topicId} pour se désabonner d'un thème
    @Transactional
    @GetMapping("/unsubscribe/{topicId}")
    public ResponseEntity<?> unsubscribeFromTopic(@PathVariable Long topicId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findWithAbonnementsByUsername(username).orElseThrow();
        Topic topic = topicRepository.findByIdWithAbonnes(topicId).orElseThrow();

        user.getAbonnements().remove(topic); // Retire le thème des abonnements de l'utilisateur
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}