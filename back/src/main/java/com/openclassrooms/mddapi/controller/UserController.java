// Contrôleur REST pour la gestion du profil utilisateur (consultation et mise à jour)
package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.dto.UserProfileDto;
import com.openclassrooms.mddapi.dto.UserProfileUpdateDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController // Indique que cette classe est un contrôleur REST
@RequestMapping("/api/profile") // Préfixe pour tous les endpoints liés au profil utilisateur
public class UserController {

    @Autowired
    private UserRepository userRepository; // Accès aux utilisateurs

    @Autowired
    private TopicRepository topicRepository; // Accès aux thèmes

    @Autowired
    private PasswordEncoder passwordEncoder; // Pour encoder les mots de passe

    @Autowired
    private UserService userService; // Service métier utilisateur

    // Endpoint GET /api/profile pour récupérer le profil de l'utilisateur connecté
    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(Principal principal) {
        String username = principal.getName(); // Récupère le nom d'utilisateur authentifié
        User user = userRepository.findWithAbonnementsByUsername(username).orElseThrow();

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        // Récupère les abonnements de l'utilisateur et les convertit en DTO
        dto.setAbonnements(
                user.getAbonnements().stream()
                        .map(topic -> new TopicDto(topic.getId(), topic.getName(), topic.getDescription()))
                        .collect(Collectors.toSet())
        );
        return ResponseEntity.ok(dto);
    }

    // Endpoint PUT /api/profile pour mettre à jour le profil utilisateur
    @PutMapping
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody UserProfileUpdateDto updates) {
        String username = principal.getName();
        User user = userRepository.findWithAbonnementsByUsername(username).orElseThrow();

        // Met à jour le nom d'utilisateur si fourni
        if (updates.getUsername() != null) {
            user.setUsername(updates.getUsername());
        }
        // Met à jour l'email si fourni
        if (updates.getEmail() != null) {
            user.setEmail(updates.getEmail());
        }
        // Met à jour le mot de passe si fourni (après encodage)
        if (updates.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updates.getPassword()));
        }
        // Gère les désabonnements si une liste d'IDs de thèmes à retirer est fournie
        if (updates.getDesabonnements() != null && !updates.getDesabonnements().isEmpty()) {
            List<Topic> toRemove = user.getAbonnements().stream()
                    .filter(t -> updates.getDesabonnements().contains(t.getId()))
                    .collect(Collectors.toList());
            user.getAbonnements().removeAll(toRemove);
        }

        userRepository.save(user); // Sauvegarde les modifications
        return ResponseEntity.ok().build();
    }
}