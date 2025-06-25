// Service pour la gestion des utilisateurs (inscription, authentification)
package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.RegisterRequestDto;
import com.openclassrooms.mddapi.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service // Indique que cette classe est un service Spring
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Injection des dépendances via le constructeur
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Inscrit un nouvel utilisateur à partir d'un DTO d'inscription
    public User register(RegisterRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername()); // Définit le nom d'utilisateur
        user.setEmail(request.getEmail()); // Définit l'email
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode le mot de passe
        return userRepository.save(user); // Sauvegarde l'utilisateur en base
    }

    // Authentifie un utilisateur par nom d'utilisateur ou email et mot de passe
    public User authenticate(String identifier, String password) {
        // Recherche l'utilisateur par nom d'utilisateur, sinon par email
        User user = userRepository.findByUsername(identifier)
                .orElseGet(() -> userRepository.findByEmail(identifier).orElse(null));
        // Vérifie si l'utilisateur existe et si le mot de passe correspond
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Identifiants invalides"); // Lève une exception si échec
    }
}