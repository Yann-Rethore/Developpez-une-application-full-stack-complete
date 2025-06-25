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

@RestController
@RequestMapping("/api/profile")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findWithAbonnementsByUsername(username).orElseThrow();

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAbonnements(
                user.getAbonnements().stream()
                        .map(topic -> new TopicDto(topic.getId(), topic.getName(), topic.getDescription()))
                        .collect(Collectors.toSet())
        );
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody UserProfileUpdateDto updates) {
        String username = principal.getName();
        User user = userRepository.findWithAbonnementsByUsername(username).orElseThrow();

        if (updates.getUsername() != null) {
            user.setUsername(updates.getUsername());
        }
        if (updates.getEmail() != null) {
            user.setEmail(updates.getEmail());
        }
        if (updates.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updates.getPassword()));
        }
        if (updates.getDesabonnements() != null && !updates.getDesabonnements().isEmpty()) {
            List<Topic> toRemove = user.getAbonnements().stream()
                    .filter(t -> updates.getDesabonnements().contains(t.getId()))
                    .collect(Collectors.toList());
            user.getAbonnements().removeAll(toRemove);
        }

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}